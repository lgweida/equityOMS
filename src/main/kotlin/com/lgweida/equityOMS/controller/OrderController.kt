package com.lgweida.equityOMS.controller

import com.lgweida.equityOMS.dto.OrderDto
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.CrossOrigin

@RestController
@CrossOrigin(origins = ["http://localhost:*", "http://192.168.*.*:*", "http://10.*.*.*:*"])
@RequestMapping("/api/orders")

class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders(): List<OrderDto> {
        return orderService.getAllOrders()
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): OrderDto? {
        return orderService.getOrderById(id)
    }

    @GetMapping("/clordid/{clOrdId}")
    fun getOrderByClOrdId(@PathVariable clOrdId: String): OrderDto? {
        return orderService.getOrderByClOrdId(clOrdId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody orderRequest: OrderRequest): OrderDto {
        return orderService.createOrder(orderRequest)
    }

    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody orderRequest: OrderRequest
    ): OrderDto? {
        return orderService.updateOrder(id, orderRequest)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Long) {
       // orderService.deleteOrder(id)
    }
}
