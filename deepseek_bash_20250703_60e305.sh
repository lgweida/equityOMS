#!/bin/bash

# Define the base directory
BASE_DIR="src/main/kotlin/com/lgweida/equityOMS"

# Create directories
mkdir -p "$BASE_DIR/config"
mkdir -p "$BASE_DIR/controller"
mkdir -p "$BASE_DIR/dto"
mkdir -p "$BASE_DIR/entity"
mkdir -p "$BASE_DIR/repository"
mkdir -p "$BASE_DIR/service"

# Create files with content

# Config - DataInitializer
cat > "$BASE_DIR/config/DataInitializer.kt" << 'EOL'
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
                    clOrdId = "ORD-001",
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
                    clOrdId = "ORD-001",
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
                    clOrdId = "ORD-002",
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
EOL

# Controller - ExecutionController
cat > "$BASE_DIR/controller/ExecutionController.kt" << 'EOL'
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
EOL

# Controller - OrderController
cat > "$BASE_DIR/controller/OrderController.kt" << 'EOL'
package com.lgweida.equityOMS.controller

import com.lgweida.equityOMS.dto.OrderDto
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders(): List<OrderDto> {
        return orderService.getAllOrders()
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): OrderDto? {
        return orderService.getOrderById(id)
    }

    @GetMapping("/clordid/{clOrdId}")
    fun getOrderByClOrdId(@PathVariable clOrdId: String): OrderDto? {
        return orderService.getOrderByClOrdId(clOrdId)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody orderRequest: OrderRequest): OrderDto {
        return orderService.createOrder(orderRequest)
    }

    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody orderRequest: OrderRequest
    ): OrderDto? {
        return orderService.updateOrder(id, orderRequest)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Long) {
        orderService.deleteOrder(id)
    }
}
EOL

# DTO - ExecutionDto
cat > "$BASE_DIR/dto/ExecutionDto.kt" << 'EOL'
package com.lgweida.equityOMS.dto

import java.time.LocalDateTime

data class ExecutionDto(
    val id: Long?,
    val execId: String,
    val clOrdId: String,
    val orderId: String?,
    val execType: Char,
    val execTransType: Char?,
    val execRefId: String?,
    val symbol: String,
    val lastQty: Double?,
    val lastPx: Double?,
    val leavesQty: Double?,
    val cumQty: Double?,
    val avgPx: Double?,
    val side: Char,
    val ordStatus: Char?,
    val transactTime: LocalDateTime,
    val createdAt: LocalDateTime
)

data class ExecutionRequest(
    val execId: String,
    val clOrdId: String,
    val orderId: String?,
    val execType: Char,
    val execTransType: Char?,
    val execRefId: String?,
    val symbol: String,
    val lastQty: Double?,
    val lastPx: Double?,
    val leavesQty: Double?,
    val cumQty: Double?,
    val avgPx: Double?,
    val side: Char,
    val ordStatus: Char?,
    val transactTime: LocalDateTime
)
EOL

# DTO - Extensions
cat > "$BASE_DIR/dto/Extensions.kt" << 'EOL'
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
        clOrdId = this.clOrdId,
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
        createdAt = this.createdAt
    )
}
EOL

# DTO - OrderDto
cat > "$BASE_DIR/dto/OrderDto.kt" << 'EOL'
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
    val clOrdId: String,
    val symbol: String,
    val side: Char,
    val transactTime: LocalDateTime,
    val orderQty: Double,
    val ordType: Char,
    val price: Double?,
    val timeInForce: String?,
    val account: String?
)
EOL

# Entity - Execution
cat > "$BASE_DIR/entity/Execution.kt" << 'EOL'
package com.lgweida.equityOMS.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "executions")
data class Execution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "exec_id", nullable = false, unique = true)
    val execId: String,

    @Column(name = "cl_ord_id", nullable = false)
    val clOrdId: String,

    @Column(name = "order_id")
    val orderId: String? = null,

    @Column(name = "exec_type", nullable = false)
    val execType: Char,

    @Column(name = "exec_trans_type")
    val execTransType: Char? = null,

    @Column(name = "exec_ref_id")
    val execRefId: String? = null,

    @Column(nullable = false)
    val symbol: String,

    @Column(name = "last_qty")
    val lastQty: Double? = null,

    @Column(name = "last_px")
    val lastPx: Double? = null,

    @Column(name = "leaves_qty")
    val leavesQty: Double? = null,

    @Column(name = "cum_qty")
    val cumQty: Double? = null,

    @Column(name = "avg_px")
    val avgPx: Double? = null,

    @Column(nullable = false)
    val side: Char,

    @Column(name = "ord_status")
    val ordStatus: Char? = null,

    @Column(name = "transact_time", nullable = false)
    val transactTime: LocalDateTime,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cl_ord_id")
    var order: Order? = null
)
EOL

# Entity - Order
cat > "$BASE_DIR/entity/Order.kt" << 'EOL'
package com.lgweida.equityOMS.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "cl_ord_id", nullable = false, unique = true)
    val clOrdId: String,

    @Column(nullable = false)
    val symbol: String,

    @Column(nullable = false)
    val side: Char,

    @Column(name = "transact_time", nullable = false)
    val transactTime: LocalDateTime,

    @Column(name = "order_qty", nullable = false)
    val orderQty: Double,

    @Column(name = "ord_type", nullable = false)
    val ordType: Char,

    @Column
    val price: Double? = null,

    @Column(name = "time_in_force")
    val timeInForce: String? = null,

    @Column
    val account: String? = null,

    @Column(name = "ord_status")
    val ordStatus: Char = '0',

    @Column(name = "cum_qty")
    val cumQty: Double = 0.0,

    @Column(name = "leaves_qty")
    val leavesQty: Double? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true ,fetch = FetchType.LAZY)
    val executions: MutableList<Execution> = mutableListOf()
    )
EOL

# Repository - ExecutionRepository
cat > "$BASE_DIR/repository/ExecutionRepository.kt" << 'EOL'
package com.lgweida.equityOMS.repository

import com.lgweida.equityOMS.entity.Execution
import org.springframework.data.jpa.repository.JpaRepository

interface ExecutionRepository : JpaRepository<Execution, Long> {
    fun findByClOrdId(clOrdId: String): List<Execution>
    fun findByExecId(execId: String): Execution?
}
EOL

# Repository - OrderRepository
cat > "$BASE_DIR/repository/OrderRepository.kt" << 'EOL'
package com.lgweida.equityOMS.repository

import com.lgweida.equityOMS.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByClOrdId(clOrdId: String): Order?
}
EOL

# Service - ExecutionService
cat > "$BASE_DIR/service/ExecutionService.kt" << 'EOL'
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
            ?: throw IllegalArgumentException("Order not found")

        val execution = Execution(
            execId = executionRequest.execId,
            clOrdId = executionRequest.clOrdId,
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
                ?: throw IllegalArgumentException("Order not found")

            val updatedExecution = existingExecution.copy(
                execId = executionRequest.execId,
                clOrdId = executionRequest.clOrdId,
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
EOL

# Service - OrderService
cat > "$BASE_DIR/service/OrderService.kt" << 'EOL'
package com.lgweida.equityOMS.service

import com.lgweida.equityOMS.dto.OrderDto
import com.lgweida.equityOMS.dto.OrderRequest
import com.lgweida.equityOMS.dto.toDto
import com.lgweida.equityOMS.entity.Order
import com.lgweida.equityOMS.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun getAllOrders(): List<OrderDto> {
        return orderRepository.findAll().map { it.toDto() }
    }

    fun getOrderById(id: Long): OrderDto? {
        return orderRepository.findById(id).orElse(null)?.toDto()
    }

    fun getOrderByClOrdId(clOrdId: String): OrderDto? {
        return orderRepository.findByClOrdId(clOrdId)?.toDto()
    }

    fun createOrder(orderRequest: OrderRequest): OrderDto {
        val order = Order(
            clOrdId = orderRequest.clOrdId,
            symbol = orderRequest.symbol,
            side = orderRequest.side,
            transactTime = orderRequest.transactTime,
            orderQty = orderRequest.orderQty,
            ordType = orderRequest.ordType,
            price = orderRequest.price,
            timeInForce = orderRequest.timeInForce,
            account = orderRequest.account
        )
        return orderRepository.save(order).toDto()
    }

    fun updateOrder(id: Long, orderRequest: OrderRequest): OrderDto? {
        return orderRepository.findById(id).map { existingOrder ->
            val updatedOrder = existingOrder.copy(
                clOrdId = orderRequest.clOrdId,
                symbol = orderRequest.symbol,
                side = orderRequest.side,
                transactTime = orderRequest.transactTime,
                orderQty = orderRequest.orderQty,
                ordType = orderRequest.ordType,
                price = orderRequest.price,
                timeInForce = orderRequest.timeInForce,
                account = orderRequest.account,
                updatedAt = LocalDateTime.now()
            )
            orderRepository.save(updatedOrder).toDto()
        }.orElse(null)
    }

    fun deleteOrder(id: Long) {
        orderRepository.deleteById(id)
    }
}
EOL

echo "Directory structure and files created successfully at $BASE_DIR"