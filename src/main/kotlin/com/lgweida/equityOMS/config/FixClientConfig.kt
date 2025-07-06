package com.lgweida.equityOMS.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import quickfix.*
import quickfix.SocketInitiator
import javax.sql.DataSource
import com.lgweida.equityOMS.fix.FixClientApplication

@Configuration
class FixClientConfig {

    @Bean
    fun sessionSettings(): SessionSettings {
        val inputStream = this::class.java.classLoader.getResourceAsStream("fix-gateway.cfg")
            ?: throw IllegalStateException("fix-gateway.cfg not found")
        return SessionSettings(inputStream)
    }

    @Bean
    fun messageStoreFactory(
        settings: SessionSettings,
        dataSource: DataSource
    ): MessageStoreFactory {
        val factory = JdbcStoreFactory(settings)
    
        // Correct reflection approach:
        try {
            val field = JdbcStoreFactory::class.java.getDeclaredField("dataSource")
            field.isAccessible = true
            field.set(factory, dataSource)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to set dataSource on JdbcStoreFactory", e)
        }
        
        return factory
    }
    @Bean
    fun logFactory(settings: SessionSettings): LogFactory {
        return FileLogFactory(settings)
    }

    @Bean
    fun messageFactory(): MessageFactory {
        return DefaultMessageFactory()
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun socketInitiator(
        fixClientApplication: FixClientApplication,
        messageStoreFactory: MessageStoreFactory,
        settings: SessionSettings,
        logFactory: LogFactory,
        messageFactory: MessageFactory
    ): SocketInitiator {
        return SocketInitiator(
            fixClientApplication,
            messageStoreFactory,
            settings,
            logFactory,
            messageFactory
        ).also {
            fixClientApplication.setInitiator(it)
        }
    }
}