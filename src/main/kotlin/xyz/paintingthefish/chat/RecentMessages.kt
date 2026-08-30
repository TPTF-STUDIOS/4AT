package xyz.paintingthefish.chat

import java.nio.ByteBuffer
import java.util.*

class RecentMessages(private val maxBufferSize: Int = 521233) {

    enum class MessageBufferErrors {
        OK, TOO_BIG, TOO_SMALL
    }

    // ArrayDeque handles chronological ordering (FIFO)
    private val messages: Queue<ByteArray> = ArrayDeque<ByteArray>(maxBufferSize)

    // Index map handles INSTANT lookups by ID without looping
    private val messageIndex: HashMap<Int, ByteArray> = HashMap(maxBufferSize)

    @Synchronized
    fun add(msg: ByteArray): MessageBufferErrors {
        if (msg.size < 2060) {
            return MessageBufferErrors.TOO_SMALL
        } else if (msg.size > 3000) {
            return MessageBufferErrors.TOO_BIG
        }

        // If cache is full, evict from both the queue AND the index map
        if (messages.size >= maxBufferSize) {
            val oldestMsg = messages.poll()
            if (oldestMsg != null) {
                val oldestId = ByteBuffer.wrap(oldestMsg).getInt()
                messageIndex.remove(oldestId) // Keep memory clean!
            }
        }

        // Grab the ID of the new message instantly from its first 4 bytes
        val newId = ByteBuffer.wrap(msg).getInt()

        messages.offer(msg)
        messageIndex[newId] = msg // Instant indexing
        return MessageBufferErrors.OK
    }

    @Synchronized
    fun getId(id: Int): ByteArray? {
        // Blazing fast O(1) lookup. No loops, no lag, no object allocation!
        return messageIndex[id]
    }
}
