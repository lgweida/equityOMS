package com.lgweida.equityOMS.dto

import com.lgweida.equityOMS.entity.Execution
import com.lgweida.equityOMS.entity.Order

fun Order.toDto(): OrderDto {
    return OrderDto(
        id = this.id,
        clOrdId = this.clOrdId,
        symbol = this.symbol,
        side = this.side,
        transactTime = this.transactTime,
        orderQty = this.orderQty,
        ordType = this.ordType,
        price = this.price,
        timeInForce = this.timeInForce,
        account = this.account,
        ordStatus = this.ordStatus,
        cumQty = this.cumQty,
        leavesQty = this.leavesQty,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        executions = this.executions.map { it.toDto() }
    )
}

fun Execution.toDto(): ExecutionDto {
    return ExecutionDto(
        id = this.id,
        execId = this.execId,
        orderId = this.orderId,
        execType = this.execType,
        execTransType = this.execTransType,
        execRefId = this.execRefId,
        symbol = this.symbol,
        lastQty = this.lastQty,
        lastPx = this.lastPx,
        leavesQty = this.leavesQty,
        cumQty = this.cumQty,
        avgPx = this.avgPx,
        side = this.side,
        ordStatus = this.ordStatus,
        transactTime = this.transactTime,
        createdAt = this.createdAt,
        clOrdId = this.order?.clOrdId
    )
}
