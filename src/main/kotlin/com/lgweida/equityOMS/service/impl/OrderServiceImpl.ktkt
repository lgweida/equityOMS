package com.lgweida.equityOMS.service.impl

import com.lgweida.equityOMS.dto.OrderDto
import com.lgweida.equityOMS.dto.ExecutionDto
import com.lgweida.equityOMS.entity.Execution
import com.lgweida.equityOMS.dto.OrderWithExecutionsDto
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.entity.Order
import com.lgweida.equityOMS.exception.OrderNotFoundException
import com.lgweida.equityOMS.repository.OrderRepository
import com.lgweida.equityOMS.service.OrderService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository
) : OrderService {

    override fun createOrder(orderRequest: OrderRequest): OrderDto {
        val order = Order(
            clOrdId = orderRequest.clOrdId,
            symbol = orderRequest.symbol,
            side = orderRequest.side,
            transactTime = LocalDateTime.now(),
            orderQty = orderRequest.orderQty,
            ordType = orderRequest.ordType,
            price = orderRequest.price,
            timeInForce = orderRequest.timeInForce,
            account = orderRequest.account
        )
        val savedOrder = orderRepository.save(order)
        return savedOrder.toDto()
    }

    override fun getOrderById(id: Long): OrderDto {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException("Order not found with id: $id") }
        return order.toDto()
    }

    override fun getOrderByClOrdId(clOrdId: String): OrderDto {
        val order = orderRepository.findByClOrdId(clOrdId)
            ?: throw OrderNotFoundException("Order not found with clOrdId: $clOrdId")
        return order.toDto()
    }

    override fun getAllOrders(): List<OrderDto> {
        return orderRepository.findAll().map { it.toDto() }
    }

    override fun getOrdersBySymbol(symbol: String): List<OrderDto> {
        return orderRepository.findAllBySymbol(symbol).map { it.toDto() }
    }

    override fun getOrdersByStatus(status: Char): List<OrderDto> {
        return orderRepository.findAllByOrdStatus(status).map { it.toDto() }
    }

    override fun getOrdersByAccount(account: String): List<OrderDto> {
        return orderRepository.findAllByAccount(account).map { it.toDto() }
    }

    override fun cancelOrder(clOrdId: String): OrderDto {
        val order = orderRepository.findByClOrdId(clOrdId)
            ?: throw OrderNotFoundException("Order not found with clOrdId: $clOrdId")
        
        val updatedOrder = order.copy(
            ordStatus = '4', // Canceled
            updatedAt = LocalDateTime.now()
        )
        
        val savedOrder = orderRepository.save(updatedOrder)
        return savedOrder.toDto()
    }
 
}
