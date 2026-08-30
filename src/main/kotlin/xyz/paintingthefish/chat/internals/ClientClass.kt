package xyz.paintingthefish.chat.internals

import org.ini4j.Wini
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.enums.Opcode
import org.java_websocket.enums.ReadyState
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.protocols.IProtocol
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.URI
import java.nio.ByteBuffer
import java.util.Base64
import javax.net.ssl.SSLSession
import kotlin.math.round

class ClientClass(conf: Wini?, uri: URI) :
    WebSocketClient(uri) {
    val serverUri: URI = uri
    override fun onOpen(handshakedata: ServerHandshake?) {
        System.out.println("Successfully connected to $serverUri")
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
