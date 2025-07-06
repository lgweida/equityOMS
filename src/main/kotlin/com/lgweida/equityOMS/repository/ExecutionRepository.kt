package com.lgweida.equityOMS.repository

import com.lgweida.equityOMS.entity.Execution
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ExecutionRepository : JpaRepository<Execution, Long> {
    @Query("SELECT e FROM Execution e WHERE e.order.clOrdId = :clOrdId")
    fun findByClOrdId(clOrdId: String): List<Execution>

    fun findByExecId(execId: String): Execution?
}