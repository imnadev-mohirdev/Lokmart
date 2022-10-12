package uz.mohirdev.driver

import android.util.Log
import org.java_websocket.WebSocket
import java.net.InetSocketAddress
import org.java_websocket.server.WebSocketServer
import org.java_websocket.handshake.ClientHandshake
import java.lang.Exception

class WebsocketServer(address: InetSocketAddress) : WebSocketServer(address) {

    private var lastMessage: String? = null

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.d("WEBSOCKET", "onClose($code, $reason, $remote)")
    }

    override fun onError(conn: WebSocket?, e: Exception?) {
        Log.d("WEBSOCKET", "onError($e)")
    }

    override fun onStart() {
        Log.d("WEBSOCKET", "onStart()")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        Log.d("WEBSOCKET", "onMessage($message)")
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        Log.d("WEBSOCKET", "onOpen()")
        lastMessage?.let {
            conn?.send(lastMessage)
        }
    }

    override fun broadcast(text: String?) {
        lastMessage = text
        super.broadcast(text)
    }
}