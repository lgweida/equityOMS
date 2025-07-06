package com.lgweida.equityOMS.websocket

import com.lgweida.equityOMS.entity.Order
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class OrderNotificationService(private val messagingTemplate: SimpMessagingTemplate) {
    fun notifyOrderUpdate(order: Order) {
        messagingTemplate.convertAndSend("/topic/orders/${order.clOrdId}", order)
    }

    fun notifyNewOrder(order: Order) {
        messagingTemplate.convertAndSend("/topic/orders", order)
    }
}