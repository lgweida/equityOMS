package com.lgweida.equityOMS.fix

import com.lgweida.equityOMS.entity.Execution
import com.lgweida.equityOMS.entity.Order
import com.lgweida.equityOMS.entity.OrderStatus
import com.lgweida.equityOMS.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import quickfix.FieldNotFound
import quickfix.Message
import quickfix.field.*
import quickfix.fix44.ExecutionReport
import quickfix.fix44.NewOrderSingle
import quickfix.fix44.OrderCancelRequest
import quickfix.fix44.OrderCancelReplaceRequest
import java.time.LocalDateTime

@Component
class FixMessageProcessor(private val orderRepository: OrderRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun processIncomingFixMessage(message: Message) {
        when (message) {
            is ExecutionReport -> processExecutionReport(message)
            else -> logger.warn("Unsupported FIX message type: ${message.javaClass.simpleName}")
        }
    }

    fun convertToFixMessage(order: Order): Message {
        // return when (order.status) {
        //     OrderStatus.NEW -> convertToNewOrderSingle(order)
        //     OrderStatus.CANCELED -> convertToOrderCancelRequest(order)
        //     OrderStatus.REPLACED -> convertToOrderCancelReplaceRequest(order)
        //     else -> throw IllegalArgumentException("Unsupported order status for FIX conversion: ${order.status}")
        // }
        return convertToNewOrderSingle(order)
    }

    private fun convertToNewOrderSingle(order: Order): NewOrderSingle {
        val nos = NewOrderSingle()

        nos.set(ClOrdID(order.clOrdId))
        nos.set(Side(order.side.toChar()))
        nos.set(TransactTime())
        nos.set(OrdType(order.ordType))

        nos.set(HandlInst('1'))
        nos.set(Symbol(order.symbol))
        nos.set(OrderQty(order.orderQty))
        order.price?.let { nos.set(Price(it)) }

        return nos
    }

    private fun convertToOrderCancelRequest(order: Order): OrderCancelRequest {
        return OrderCancelRequest(
            OrigClOrdID(order.origClOrdId ?: order.clOrdId),
            ClOrdID(order.clOrdId),
            Side(order.side.toChar()),
            TransactTime()
        ).apply {
            set(OrderQty(order.orderQty))
            set(Symbol(order.symbol))
        }
    }

    private fun convertToOrderCancelReplaceRequest(order: Order): OrderCancelReplaceRequest {
        val ocrr = OrderCancelReplaceRequest(
            OrigClOrdID(order.origClOrdId ?: order.clOrdId),
            ClOrdID(order.clOrdId),
            Side(order.side.toChar()),
            TransactTime(),
            OrdType('1')
        )

        ocrr.set(Symbol(order.symbol))
        ocrr.set(OrderQty(order.orderQty))
        order.price?.let { ocrr.set(Price(it)) }  // Safe handling of nullable price

        return ocrr
    }

    private fun processExecutionReport(er: ExecutionReport) {
        try {
            val clOrdId = er.getClOrdID().value
            val order = orderRepository.findByClOrdId(clOrdId)
                ?: throw IllegalStateException("Order not found for ClOrdID: $clOrdId")

            val execType = er.getExecType().value
            val execId = er.getExecID().value

            when (execType) {
                '0' -> processNewExecution(order, er, execId) // New
                '4' -> processCanceledExecution(order, er, execId) // Canceled
                '5' -> processReplacedExecution(order, er, execId) // Replaced
                'F' -> processTradeExecution(order, er, execId) // Trade
                else -> logger.warn("Unsupported ExecType: $execType")
            }

            orderRepository.save(order)
        } catch (e: FieldNotFound) {
            logger.error("Missing required field in ExecutionReport", e)
        }
    }

    private fun processNewExecution(order: Order, er: ExecutionReport, execId: String) {
        //order.status = OrderStatus.NEW
        // Add any other processing for new order ACK
    }

    private fun processCanceledExecution(order: Order, er: ExecutionReport, execId: String) {
        //order.status = OrderStatus.CANCELED
        //order.updatedAt = LocalDateTime.now()
        addExecution(order, er, execId, '4')
    }

    private fun processReplacedExecution(order: Order, er: ExecutionReport, execId: String) {
        //order.status = OrderStatus.REPLACED
        //order.updatedAt = LocalDateTime.now()
        addExecution(order, er, execId, '5')
    }

    private fun processTradeExecution(order: Order, er: ExecutionReport, execId: String) {
        val leavesQty = er.getLeavesQty().value
        //order.status = if (leavesQty == 0.0) OrderStatus.FILLED else OrderStatus.PARTIALLY_FILLED
       // order.updatedAt = LocalDateTime.now()
        addExecution(order, er, execId, 'F')
    }

    private fun addExecution(order: Order, er: ExecutionReport, execId: String, execType: Char) {
        // val execution = Execution(
        //     order = order,
        //     execId = execId,
        //     execType = execType,
        //     lastQty = er.getLastQty().value,
        //     lastPx = er.getLastPx().value,
        //     cumQty = er.getCumQty().value,
        //     avgPx = er.getAvgPx().value,
        //     leavesQty = er.getLeavesQty().value
        // )
        // order.executions.add(execution)
    }
    private fun convertSide(jsonSide: String): Char {
       return when (jsonSide.uppercase()) {
            "BUY" -> Side.BUY
            "SELL" -> Side.SELL
            "SELL_SHORT" -> Side.SELL_SHORT
             else -> throw IllegalArgumentException("Invalid side: $jsonSide")
    }
}

    private fun convertOrderType(jsonOrderType: String): Char {
        return when (jsonOrderType.uppercase()) {
            "MARKET" -> OrdType.MARKET
            "LIMIT" -> OrdType.LIMIT
            "STOP_LIMIT" -> OrdType.STOP_LIMIT
            "MARKET_ON_CLOSE" -> OrdType.MARKET_ON_CLOSE
            else -> throw IllegalArgumentException("Invalid order type: $jsonOrderType")
        }
    }
}