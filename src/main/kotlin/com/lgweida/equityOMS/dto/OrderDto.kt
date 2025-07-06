package com.lgweida.equityOMS.dto

import java.time.LocalDateTime

data class OrderDto(
    val id: Long?,
    val clOrdId: String,
    val symbol: String,
    val side: Char,
    val transactTime: LocalDateTime,
    val orderQty: Double,
    val ordType: Char,
    val price: Double?,
    val timeInForce: String?,
    val account: String?,
    val ordStatus: Char,
    val cumQty: Double,
    val leavesQty: Double?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val executions: List<ExecutionDto> = emptyList()
)

data class OrderRequest(
    //val clOrdId: String,
    val symbol: String,
    val side: Char,
    val transactTime: LocalDateTime,
    val orderQty: Double,
    val ordType: Char,
    val price: Double?,
    val timeInForce: String?,
    val account: String?
)
