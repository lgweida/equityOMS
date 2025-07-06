package com.lgweida.equityOMS.config

import com.lgweida.equityOMS.fix.util.ClOrdIdGenerator
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.beans.factory.annotation.Value

@Configuration
class FixConfig {
    @Bean
    fun clOrdIdGenerator(
        @Value("\${app.fix.clordid.prefix}") prefix: String,
        @Value("\${app.fix.clordid.trader}") trader: String
    ) = ClOrdIdGenerator(prefix, trader)
}