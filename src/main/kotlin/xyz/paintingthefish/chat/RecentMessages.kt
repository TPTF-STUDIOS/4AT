package xyz.paintingthefish.chat

import java.nio.ByteBuffer
import java.util.*

class RecentMessages {
    constructor(maxSize: Int) {
        maxBufferSize = maxSize
    }
    constructor()

    enum class MessageBufferErrors {
        OK,
        TOO_BIG,
        TOO_SMALL,
    }

    private var maxBufferSize: Int = 1 // to be overridden by constructor
    private val messages: Queue<ByteArray> = ArrayDeque<ByteArray>(maxBufferSize)

    @Synchronized
    fun add(msg: ByteArray): MessageBufferErrors {
        if (msg.size < 2060) {
            System.err.println("Message size is too short! :(")
            return MessageBufferErrors.TOO_SMALL
        } else if (msg.size > 3000) {
            System.err.println("Message size is too long! :(")
            return MessageBufferErrors.TOO_BIG
        }
        if (messages.size >= maxBufferSize) {
            messages.poll() // Discard the oldest message
        }
        messages.offer(msg) // Add the newest message
        return MessageBufferErrors.OK
    }

    fun getId(id: Int): ByteArray? {
        for (barr in messages) {
            val messageId = ByteBuffer.wrap(barr).getInt()
            if (messageId == id) {
                return barr
            }
        }
        return null
    }
}
