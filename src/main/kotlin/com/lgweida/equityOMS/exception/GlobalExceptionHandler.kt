package com.lgweida.equityOMS.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFoundException(ex: OrderNotFoundException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            timestamp = LocalDateTime.now(),
            message = ex.message,
            details = request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            timestamp = LocalDateTime.now(),
            message = ex.message,
            details = request.getDescription(false)
        )
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorDetails(
    val timestamp: LocalDateTime,
    val message: String?,
    val details: String
)
