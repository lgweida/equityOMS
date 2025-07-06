package com.lgweida.equityOMS.controller

import com.lgweida.equityOMS.dto.ExecutionDto
import com.lgweida.equityOMS.dto.ExecutionRequest
import com.lgweida.equityOMS.service.ExecutionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/executions")
class ExecutionController(private val executionService: ExecutionService) {

    @GetMapping
    fun getAllExecutions(): List<ExecutionDto> {
        return executionService.getAllExecutions()
    }

    @GetMapping("/{id}")
    fun getExecutionById(@PathVariable id: Long): ExecutionDto? {
        return executionService.getExecutionById(id)
    }

    @GetMapping("/execid/{execId}")
    fun getExecutionByExecId(@PathVariable execId: String): ExecutionDto? {
        return executionService.getExecutionByExecId(execId)
    }

    @GetMapping("/clordid/{clOrdId}")
    fun getExecutionsByClOrdId(@PathVariable clOrdId: String): List<ExecutionDto> {
        return executionService.getExecutionsByClOrdId(clOrdId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createExecution(@RequestBody executionRequest: ExecutionRequest): ExecutionDto {
        return executionService.createExecution(executionRequest)
    }

    @PutMapping("/{id}")
    fun updateExecution(
        @PathVariable id: Long,
        @RequestBody executionRequest: ExecutionRequest
    ): ExecutionDto? {
        return executionService.updateExecution(id, executionRequest)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExecution(@PathVariable id: Long) {
        executionService.deleteExecution(id)
    }
}
