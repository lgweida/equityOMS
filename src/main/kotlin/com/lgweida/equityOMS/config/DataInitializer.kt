package com.lgweida.equityOMS.config

import com.lgweida.equityOMS.entity.Execution
import com.lgweida.equityOMS.entity.Order
import com.lgweida.equityOMS.repository.ExecutionRepository
import com.lgweida.equityOMS.repository.OrderRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        orderRepository: OrderRepository,
        executionRepository: ExecutionRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            // Create sample orders
            val order1 = orderRepository.save(
                Order(
                    clOrdId = "ORD-001",
                    symbol = "AAPL",
                    side = '1', // Buy
                    transactTime = LocalDateTime.now(),
                    orderQty = 100.0,
                    ordType = '2', // Limit order
                    price = 150.0,
                    timeInForce = "DAY",
                    account = "ACC-001"
                )
            )

            val order2 = orderRepository.save(
                Order(
                    clOrdId = "ORD-002",
                    symbol = "MSFT",
                    side = '2', // Sell
                    transactTime = LocalDateTime.now(),
                    orderQty = 50.0,
                    ordType = '1', // Market order
                    timeInForce = "IOC",
                    account = "ACC-002"
                )
            )

            // Create sample executions
            executionRepository.saveAll(listOf(
                Execution(
                    execId = "EXEC-001",
                    orderId = order1.id.toString(),
                    execType = '0', // New
                    execTransType = '0', // New
                    symbol = "AAPL",
                    lastQty = 50.0,
                    lastPx = 150.0,
                    leavesQty = 50.0,
                    cumQty = 50.0,
                    avgPx = 150.0,
                    side = '1', // Buy
                    ordStatus = '1', // Partially filled
                    transactTime = LocalDateTime.now(),
                    order = order1
                ),
                Execution(
                    execId = "EXEC-002",
                    orderId = order1.id.toString(),
                    execType = 'F', // Trade
                    execTransType = '0', // New
                    symbol = "AAPL",
                    lastQty = 50.0,
                    lastPx = 150.0,
                    leavesQty = 0.0,
                    cumQty = 100.0,
                    avgPx = 150.0,
                    side = '1', // Buy
                    ordStatus = '2', // Filled
                    transactTime = LocalDateTime.now(),
                    order = order1
                ),
                Execution(
                    execId = "EXEC-003",
                    orderId = order2.id.toString(),
                    execType = 'F', // Trade
                    execTransType = '0', // New
                    symbol = "MSFT",
                    lastQty = 50.0,
                    lastPx = 250.0,
                    leavesQty = 0.0,
                    cumQty = 50.0,
                    avgPx = 250.0,
                    side = '2', // Sell
                    ordStatus = '2', // Filled
                    transactTime = LocalDateTime.now(),
                    order = order2
                )
            ))
        }
    }
}
