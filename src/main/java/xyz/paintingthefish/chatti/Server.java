package xyz.paintingthefish.chatti;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import xyz.paintingthefish.chatti.Shared;
import java.util.*;

public class Server extends WebSocketServer {
    public void onStart() {
        System.err.println("CHATTi server starting on port " + String.valueOf(getPort()));
    }

    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.err.printf("[INFO] client %s disconnected. (code %d)\n", Shared.get_wini_from_str(conn.getAttachment()).get("info", "conn_id"), code);

    }

    public void onError(WebSocket conn, Exception err) {
        conn.close();
    }

    public void onMessage(WebSocket conn, String msg) {}

    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.setAttachment("");
    }
}
