package com.lgweida.equityOMS.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "cl_ord_id", nullable = false, unique = true)
    val clOrdId: String,

    @Column(name = "order_id")
    val orderId: String? = null,

    @Column(name = "orig_cl_ord_id")
    val origClOrdId: String? = null,

    @Column(nullable = false)
    val symbol: String,

    @Column(nullable = false)
    val side: Char,

    @Column(name = "transact_time", nullable = false)
    val transactTime: LocalDateTime,

    @Column(name = "order_qty", nullable = false)
    val orderQty: Double,

    @Column(name = "ord_type", nullable = false)
    val ordType: Char,

    @Column
    val price: Double? = null,

    @Column(name = "time_in_force")
    val timeInForce: String? = null,

    @Column
    val account: String? = null,

    @Column(name = "ord_status")
    val ordStatus: Char = '0',

    @Column(name = "cum_qty")
    val cumQty: Double = 0.0,

    @Column(name = "leaves_qty")
    val leavesQty: Double? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true ,fetch = FetchType.LAZY)
    val executions: MutableList<Execution> = mutableListOf()
) 

enum class OrderStatus {
    NEW, PARTIALLY_FILLED, FILLED, CANCELED, REJECTED, PENDING_CANCEL, REPLACED
}
