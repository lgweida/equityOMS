package com.lgweida.equityOMS.dto

import java.time.LocalDateTime

data class ExecutionDto(
    val id: Long?,
    val execId: String,
    val orderId: String?,
    val execType: Char,
    val execTransType: Char?,
    val execRefId: String?,
    val symbol: String,
    val lastQty: Double?,
    val lastPx: Double?,
    val leavesQty: Double?,
    val cumQty: Double?,
    val avgPx: Double?,
    val side: Char,
    val ordStatus: Char?,
    val transactTime: LocalDateTime,
    val createdAt: LocalDateTime,
    val clOrdId: String?  // Now optional since it comes from the order
)

data class ExecutionRequest(
    val execId: String,
    val orderId: String?,
    val execType: Char,
    val execTransType: Char?,
    val execRefId: String?,
    val symbol: String,
    val lastQty: Double?,
    val lastPx: Double?,
    val leavesQty: Double?,
    val cumQty: Double?,
    val avgPx: Double?,
    val side: Char,
    val ordStatus: Char?,
    val transactTime: LocalDateTime,
    val clOrdId: String  // Still needed for creating the relationship
)
