package com.lgweida.equityOMS.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import jakarta.persistence.*  // Note: using jakarta instead of javax for Spring Boot 3+
import java.time.Instant

@Entity
@Table(name = "fix_messages")
data class FixMessage(
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val sessionId: String,
    val messageType: String,
    val senderCompId: String,
    val targetCompId: String,
    val messageContent: String,
    val direction: String, // INBOUND or OUTBOUND
    val timestamp: java.time.Instant = java.time.Instant.now()
)

@Repository
interface FixMessageRepository : JpaRepository<FixMessage, Long>