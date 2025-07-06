package com.lgweida.equityOMS.service

import com.lgweida.equityOMS.dto.ExecutionDto
import com.lgweida.equityOMS.dto.ExecutionRequest
import com.lgweida.equityOMS.dto.toDto
import com.lgweida.equityOMS.entity.Execution
import com.lgweida.equityOMS.repository.ExecutionRepository
import com.lgweida.equityOMS.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ExecutionService(
    private val executionRepository: ExecutionRepository,
    private val orderRepository: OrderRepository
) {

    fun getAllExecutions(): List<ExecutionDto> {
        return executionRepository.findAll().map { it.toDto() }
    }

    fun getExecutionById(id: Long): ExecutionDto? {
        return executionRepository.findById(id).orElse(null)?.toDto()
    }

    fun getExecutionByExecId(execId: String): ExecutionDto? {
        return executionRepository.findByExecId(execId)?.toDto()
    }

    fun getExecutionsByClOrdId(clOrdId: String): List<ExecutionDto> {
        return executionRepository.findByClOrdId(clOrdId).map { it.toDto() }
    }

    fun createExecution(executionRequest: ExecutionRequest): ExecutionDto {
        val order = orderRepository.findByClOrdId(executionRequest.clOrdId)
            ?: throw IllegalArgumentException("Order with clOrdId ${executionRequest.clOrdId} not found")

        val execution = Execution(
            execId = executionRequest.execId,
            orderId = executionRequest.orderId,
            execType = executionRequest.execType,
            execTransType = executionRequest.execTransType,
            execRefId = executionRequest.execRefId,
            symbol = executionRequest.symbol,
            lastQty = executionRequest.lastQty,
            lastPx = executionRequest.lastPx,
            leavesQty = executionRequest.leavesQty,
            cumQty = executionRequest.cumQty,
            avgPx = executionRequest.avgPx,
            side = executionRequest.side,
            ordStatus = executionRequest.ordStatus,
            transactTime = executionRequest.transactTime,
            order = order
        )

        return executionRepository.save(execution).toDto()
    }

    fun updateExecution(id: Long, executionRequest: ExecutionRequest): ExecutionDto? {
        return executionRepository.findById(id).map { existingExecution ->
            val order = orderRepository.findByClOrdId(executionRequest.clOrdId)
                ?: throw IllegalArgumentException("Order with clOrdId ${executionRequest.clOrdId} not found")

            val updatedExecution = existingExecution.copy(
                execId = executionRequest.execId,
                orderId = executionRequest.orderId,
                execType = executionRequest.execType,
                execTransType = executionRequest.execTransType,
                execRefId = executionRequest.execRefId,
                symbol = executionRequest.symbol,
                lastQty = executionRequest.lastQty,
                lastPx = executionRequest.lastPx,
                leavesQty = executionRequest.leavesQty,
                cumQty = executionRequest.cumQty,
                avgPx = executionRequest.avgPx,
                side = executionRequest.side,
                ordStatus = executionRequest.ordStatus,
                transactTime = executionRequest.transactTime,
                order = order
            )
            executionRepository.save(updatedExecution).toDto()
        }.orElse(null)
    }

    fun deleteExecution(id: Long) {
        executionRepository.deleteById(id)
    }
}