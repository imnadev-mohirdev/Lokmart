package uz.mohirdev.lokmart.data.socket

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebsocketClient(serverURI: URI, private val callback: (coordinate: String) -> Unit) : WebSocketClient(serverURI) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WEBSOCKET", "onOpen()")
    }

    override fun onMessage(message: String?) {
        Log.d("WEBSOCKET", "onMessage($message)")
        callback(message ?: return)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("WEBSOCKET", "onClose($code, $reason, $remote)")
    }

    override fun onError(ex: Exception?) {
        Log.d("WEBSOCKET", "onError($ex)")
    }
}