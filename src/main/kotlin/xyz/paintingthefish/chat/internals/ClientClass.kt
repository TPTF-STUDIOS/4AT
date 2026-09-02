package xyz.paintingthefish.chat.internals

import org.ini4j.Wini
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

@Suppress("unused")
class ClientClass(conf: Wini?, uri: URI) :
    WebSocketClient(uri) {
    val serverUri: URI = uri
    override fun onOpen(handshakedata: ServerHandshake?) {
        println("Successfully connected to $serverUri")
    }

    override fun onMessage(message: String?) {
        TODO("Not yet implemented")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onError(ex: Exception?) {
        TODO("Not yet implemented")
    }
}
