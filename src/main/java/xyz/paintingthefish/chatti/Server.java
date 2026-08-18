package xyz.paintingthefish.chatti;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import java.net.InetSocketAddress;
import java.util.*;

public class Server extends WebSocketServer {
    public void onStart() {
        System.err.println("Server starting on port " + String.valueOf(getPort()));
    }

    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.err.printf("[INFO] client disconnected. (code %d)", code);
    }

    public void onError(WebSocket conn, Exception err) {}

    public void onMessage(WebSocket conn, String msg) {}

    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.setAttachment("");
    }
}
