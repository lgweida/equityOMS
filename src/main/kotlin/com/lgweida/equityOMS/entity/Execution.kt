package com.lgweida.equityOMS.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "executions")
data class Execution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "exec_id", nullable = false, unique = true)
    val execId: String,

    @Column(name = "order_id")
    val orderId: String? = null,

    @Column(name = "exec_type", nullable = false)
    val execType: Char,

    @Column(name = "exec_trans_type")
    val execTransType: Char? = null,

    @Column(name = "exec_ref_id")
    val execRefId: String? = null,

    @Column(nullable = false)
    val symbol: String,

    @Column(name = "last_qty")
    val lastQty: Double? = null,

    @Column(name = "last_px")
    val lastPx: Double? = null,

    @Column(name = "leaves_qty")
    val leavesQty: Double? = null,

    @Column(name = "cum_qty")
    val cumQty: Double? = null,

    @Column(name = "avg_px")
    val avgPx: Double? = null,

    @Column(nullable = false)
    val side: Char,

    @Column(name = "ord_status")
    val ordStatus: Char? = null,

    @Column(name = "transact_time", nullable = false)
    val transactTime: LocalDateTime,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_ord_id", referencedColumnName = "cl_ord_id")
    var order: Order? = null
)
