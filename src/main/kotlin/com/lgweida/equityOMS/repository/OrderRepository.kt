package com.lgweida.equityOMS.repository

import com.lgweida.equityOMS.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByClOrdId(clOrdId: String): Order?
}
