package xyz.paintingthefish.chatti

import java.nio.ByteBuffer
import java.util.*

class RecentMessages {
    constructor(maxSize: Int) {
        maxBufferSize = maxSize
    }

    constructor()

    private var maxBufferSize = 0
    private val messages: Queue<ByteArray> = ArrayDeque<ByteArray>(maxBufferSize)

    @Synchronized
    fun add(msg: ByteArray?) {
        if (messages.size >= maxBufferSize) {
            messages.poll() // Discard the oldest message
        }
        messages.offer(msg) // Add the newest message
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
