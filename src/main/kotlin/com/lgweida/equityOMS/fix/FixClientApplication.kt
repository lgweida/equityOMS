package com.lgweida.equityOMS.fix

import org.slf4j.LoggerFactory
import quickfix.*
import quickfix.field.MsgSeqNum
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class FixClientApplication : Application {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var socketInitiator: SocketInitiator
    private val outgoingSeqNum = AtomicInteger(1)
    private val incomingSeqNum = AtomicInteger(1)

    fun setInitiator(initiator: SocketInitiator) {
        this.socketInitiator = initiator
    }

    fun start() {
        if (!isInitialized()) {
            throw IllegalStateException("FIX initiator not initialized")
        }
        if (!socketInitiator.isLoggedOn) {
            logger.info("Starting FIX session...")
            socketInitiator.start()
        }
    }

    fun stop() {
        if (isInitialized() && socketInitiator.isLoggedOn) {
            logger.info("Stopping FIX session...")
            socketInitiator.stop()
        }
    }

    override fun onCreate(sessionID: SessionID) {
        logger.info("FIX Session created: $sessionID")
    }

    override fun onLogon(sessionID: SessionID) {
        logger.info("FIX Session logged on: $sessionID")
        // Reset sequence numbers on logon (optional, depends on your FIX server requirements)
        //resetSequenceNumbers()
    }

    override fun onLogout(sessionID: SessionID) {
        logger.info("FIX Session logged out: $sessionID")
    }

    override fun toAdmin(message: Message, sessionID: SessionID) {
        logger.debug("FIX toAdmin [${sessionID}]: ${message.toString()}")
        manageSequenceNumbers(message, Direction.OUTGOING)
    }

    override fun fromAdmin(message: Message, sessionID: SessionID) {
        logger.debug("FIX fromAdmin [${sessionID}]: ${message.toString()}")
        manageSequenceNumbers(message, Direction.INCOMING)
    }

    override fun toApp(message: Message, sessionID: SessionID) {
        logger.debug("FIX toApp [${sessionID}]: ${message.toString()}")
        manageSequenceNumbers(message, Direction.OUTGOING)
    }

    override fun fromApp(message: Message, sessionID: SessionID) {
        logger.debug("FIX fromApp [${sessionID}]: ${message.toString()}")
        manageSequenceNumbers(message, Direction.INCOMING)
    }

    fun sendMessage(message: Message): Boolean {
        if (!isLoggedOn()) {
            logger.error("Cannot send message: FIX session is not logged on")
            throw IllegalStateException("FIX session is not logged on")
        }
        
        val sessions = socketInitiator.getSessions()
        if (sessions.isEmpty()) {
            logger.error("No active FIX sessions available")
            throw IllegalStateException("No active FIX sessions available")
        }
        
        try {
            logger.info("Sending FIX message: ${message.toString()}")
            Session.sendToTarget(message, sessions.first())
            return true
        } catch (e: SessionNotFound) {
            logger.error("Failed to send FIX message: ${e.message}", e)
            throw IllegalStateException("Failed to send FIX message: ${e.message}", e)
        } catch (e: Exception) {
            logger.error("Unexpected error sending FIX message", e)
            throw RuntimeException("Unexpected error sending FIX message", e)
        }
    }

    fun isInitialized(): Boolean = ::socketInitiator.isInitialized

    fun isLoggedOn(): Boolean = isInitialized() && socketInitiator.isLoggedOn

    fun getSessionStatus(): String {
        return when {
            !isInitialized() -> "Not initialized"
            isLoggedOn() -> "Logged on (${socketInitiator.getSessions().size} active sessions)"
            else -> "Logged off"
        }
    }

    fun resetSequenceNumbers() {
        outgoingSeqNum.set(1)
        incomingSeqNum.set(1)
        logger.info("Reset FIX sequence numbers to 1")
    }

    fun getNextOutgoingSeqNum(): Int = outgoingSeqNum.getAndIncrement()

    fun getCurrentIncomingSeqNum(): Int = incomingSeqNum.get()

    private enum class Direction { INCOMING, OUTGOING }

    private fun manageSequenceNumbers(message: Message, direction: Direction) {
        try {
            when (direction) {
                Direction.OUTGOING -> {
                    val seqNum = getNextOutgoingSeqNum()
                    message.header.setInt(MsgSeqNum.FIELD, seqNum)
                    logger.trace("Set outgoing sequence number: $seqNum")
                }
                Direction.INCOMING -> {
                    if (message.header.isSetField(MsgSeqNum.FIELD)) {
                        val seqNum = message.header.getInt(MsgSeqNum.FIELD)
                        incomingSeqNum.set(seqNum + 1)
                        logger.trace("Updated incoming sequence number: ${seqNum + 1}")
                    }
                }
            }
        } catch (e: FieldNotFound) {
            logger.warn("Sequence number management error: ${e.message}")
        }
    }
}