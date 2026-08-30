package xyz.paintingthefish.chat

import org.ini4j.Wini
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.util.Base64
import kotlin.math.round

class ServerClass(conf: Wini?, recentMessageCacheSize: Int) :
    WebSocketServer(InetSocketAddress("::", Shared.defaultPort)) {
    var recentMessagesBuffer: RecentMessages? = RecentMessages(recentMessageCacheSize)
    var cfg: Wini? = conf
    val base64Decoder: Base64.Decoder = Base64.getDecoder()
    val base64Encoder: Base64.Encoder = Base64.getEncoder()
    val base64SizeOverhead: Float = (4.0f / 3.0f)

    val OK: ByteArray = byteArrayOf(0x00, 0x00)
    val TOO_SHORT: ByteArray = byteArrayOf(0x00, 0x0A)
    val TOO_LONG: ByteArray = byteArrayOf(0x00, 0x0B)
    val PARSING_ERROR: ByteArray = byteArrayOf(0x00, 0x0C)

    override fun onStart() {
        System.err.println("ЧAT server starting on port " + getPort().toString())
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        System.err.printf(
            "[INFO] client %s disconnected. (code %d)\n",
            Shared.getWiniFromStr(conn.getAttachment<String>()).get("info", "conn_id"),
            code
        )
    }

    override fun onError(conn: WebSocket, err: Exception?) {
        conn.close()
    }

    override fun onMessage(conn: WebSocket, message: String) {
        if (round(message.length / base64SizeOverhead) <= 1) {
            conn.send(base64Encoder.encode(TOO_SHORT))
            return
        } else if (round(message.length / base64SizeOverhead) >= 1048576) {
            conn.send(base64Encoder.encode(TOO_LONG))
            return
        }
        try {
            val decodedMessage: ByteArray = base64Decoder.decode(message)
            val userData: Wini = Wini(conn.getAttachment<String>().reader())
        } catch (e: Exception) {
            e.printStackTrace()
            conn.send(base64Encoder.encode(PARSING_ERROR))
            return
        }
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        conn.setAttachment<String>("")
    }
}
