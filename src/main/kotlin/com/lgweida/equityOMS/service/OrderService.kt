package com.lgweida.equityOMS.service

import com.lgweida.equityOMS.dto.OrderDto
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.dto.toDto
import com.lgweida.equityOMS.entity.Order
import com.lgweida.equityOMS.repository.OrderRepository
import com.lgweida.equityOMS.fix.util.ClOrdIdGenerator
import com.lgweida.equityOMS.fix.FixClientApplication
import com.lgweida.equityOMS.fix.FixMessageProcessor
import com.lgweida.equityOMS.websocket.OrderNotificationService
import com.lgweida.equityOMS.exception.OrderNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(private val orderRepository: OrderRepository,
    private val notificationService: OrderNotificationService,
    private val fixMessageProcessor: FixMessageProcessor,
    private val fixClient: FixClientApplication,
    private val clOrdIdGenerator: ClOrdIdGenerator) {

    fun getAllOrders(): List<OrderDto> {
        return orderRepository.findAll().map { it.toDto() }
    }

    fun getOrderById(id: Long): OrderDto? {
        return orderRepository.findById(id).orElse(null)?.toDto()
    }

    fun getOrderByClOrdId(clOrdId: String): OrderDto? {
        return orderRepository.findByClOrdId(clOrdId)?.toDto()
    }

    fun createOrder(orderRequest: OrderRequest): OrderDto {
        val clOrdId = clOrdIdGenerator.generate()
        println(orderRequest)
        val order = Order(
            clOrdId = clOrdId,
            symbol = orderRequest.symbol,
            side = orderRequest.side,
            transactTime = orderRequest.transactTime,
            orderQty = orderRequest.orderQty,
            ordType = orderRequest.ordType,
            price = orderRequest.price,
            timeInForce = orderRequest.timeInForce,
            account = orderRequest.account
        )
        val savedOrder = orderRepository.save(order)
        val fixMessage = fixMessageProcessor.convertToFixMessage(savedOrder)
        fixClient.sendMessage(fixMessage)
        notificationService.notifyNewOrder(savedOrder)
        return savedOrder.toDto()
    }

    fun updateOrder(id: Long, orderRequest: OrderRequest): OrderDto? {
        val clOrdId = clOrdIdGenerator.generate()
        return orderRepository.findById(id).map { existingOrder ->
            val updatedOrder = existingOrder.copy(
                clOrdId = clOrdId,
                orderId = existingOrder.orderId,
                origClOrdId = existingOrder.clOrdId,
                symbol = existingOrder.symbol,
                side = existingOrder.side,
                transactTime = orderRequest.transactTime,
                orderQty = orderRequest.orderQty,
                ordType = orderRequest.ordType,
                price = orderRequest.price,
                timeInForce = orderRequest.timeInForce,
                account = existingOrder.account,
                updatedAt = LocalDateTime.now()
            )
            orderRepository.save(updatedOrder).toDto()
        }.orElse(null)
    }

    fun cancelOrder(id: Long) {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException("Order not found with id: $id") }

        // Generate a new ClOrdID for the cancel request (FIX protocol requirement)
        val clOrdId = clOrdIdGenerator.generate()
        
        // Build the FIX Order Cancel Request message
        // val fixCancelMessage = fixMessageProcessor.createCancelRequest(
        //     origClOrdId = order.clOrdId,  // Original Client Order ID
        //     clOrdId = clOrdId,            // New Client Order ID for the cancel
        //     symbol = order.symbol,
        //     side = order.side,
        //     orderQty = order.orderQty
        // )

        // Send the FIX message via fixClient (like createOrder)
       // fixClient.sendMessage(fixCancelMessage)

        // Optional: Update order status to "PENDING_CANCEL" (if not immediately confirmed)
       // order.clOrdId = OrderStatus.PENDING_CANCEL
        orderRepository.save(order)

        // Notify (e.g., log or send a WebSocket update)
       // notificationService.notifyOrderCancellation(order)
        //orderRepository.deleteById(id)
    }
}