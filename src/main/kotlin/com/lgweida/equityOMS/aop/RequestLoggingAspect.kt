package com.lgweida.equityOMS.aop

import org.aspectj.lang.JoinPoint
import org.springframework.context.annotation.Profile
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.dto.ExecutionRequest

@Aspect
@Component
@Profile("!prod")  // <-- This disables the aspect in 'prod' profile
class RequestLoggingAspect {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    fun logRequest(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        
        logger.info(
            """
            [Request Log]
            Method: ${request.method}
            Endpoint: ${request.requestURI}
            Headers: ${request.headerNames.toList().associateWith { request.getHeader(it) }}
            Path Variables: ${request.parameterMap.filter { it.key.startsWith("id") || it.key.startsWith("clOrdId") }}
            Request Body: ${joinPoint.args.find { it is OrderRequest || it is ExecutionRequest } ?: "None"}
            """.trimIndent()
        )
    }
}