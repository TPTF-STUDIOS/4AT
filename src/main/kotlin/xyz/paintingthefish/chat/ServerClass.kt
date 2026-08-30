package xyz.paintingthefish.chat

import org.ini4j.Wini
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class ServerClass(conf: Wini?, recentMessageCacheSize: Int) :
    WebSocketServer(InetSocketAddress("::", Shared.defaultPort)) {
    var recentMessagesBuffer: RecentMessages? = RecentMessages(recentMessageCacheSize)
    var cfg: Wini? = conf

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

    override fun onMessage(conn: WebSocket?, message: String?) {
        print(message!!)
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        conn.setAttachment<String?>("")
    }
}
