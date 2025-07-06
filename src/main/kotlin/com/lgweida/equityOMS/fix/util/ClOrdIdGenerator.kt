package com.lgweida.equityOMS.fix.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class ClOrdIdGenerator(
    private val systemPrefix: String = "TR",
    private val traderId: String = "01"
) {
    // Thread-safe sequence counter
    private val sequence = AtomicInteger(0)
    
    // Timestamp formatter (optimized as val)
    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS")

    fun generate(): String {
        return buildString {
            append(systemPrefix)
            append(traderId)
            append("_")
            append(LocalDateTime.now().format(timestampFormatter))
            append("_")
            append(sequence.incrementAndGet().toString().padStart(6, '0'))
        }
    }
}