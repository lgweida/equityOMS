package com.lgweida.equityOMS.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import quickfix.Message
import quickfix.SessionID
import quickfix.field.MsgType
import quickfix.field.SenderCompID
import quickfix.field.TargetCompID
import java.time.Instant
import com.lgweida.equityOMS.repository.FixMessageRepository
import com.lgweida.equityOMS.repository.FixMessage

@Service
class FixMessageService(private val fixMessageRepository: FixMessageRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun persistInboundMessage(message: Message, sessionId: SessionID) {
        try {
            val fixMessage = FixMessage(
                sessionId = sessionId.toString(),
                messageType = message.getHeader().getString(MsgType.FIELD),
                senderCompId = message.getHeader().getString(SenderCompID.FIELD),
                targetCompId = message.getHeader().getString(TargetCompID.FIELD),
                messageContent = message.toString(),
                direction = "INBOUND"
            )
            fixMessageRepository.save(fixMessage)
        } catch (e: Exception) {
            log.error("Error persisting inbound FIX message", e)
        }
    }

    fun persistOutboundMessage(message: Message, sessionId: SessionID) {
        try {
            val fixMessage = FixMessage(
                sessionId = sessionId.toString(),
                messageType = message.getHeader().getString(MsgType.FIELD),
                senderCompId = message.getHeader().getString(SenderCompID.FIELD),
                targetCompId = message.getHeader().getString(TargetCompID.FIELD),
                messageContent = message.toString(),
                direction = "OUTBOUND"
            )
            fixMessageRepository.save(fixMessage)
        } catch (e: Exception) {
            log.error("Error persisting outbound FIX message", e)
        }
    }
}