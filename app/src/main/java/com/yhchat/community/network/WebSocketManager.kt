package com.yhchat.community.network

import okhttp3.*
import timber.log.Timber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebSocketManager(
    private val baseUrl: String,
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val _messageFlow = MutableStateFlow<String?>(null)
    val messageFlow = _messageFlow.asStateFlow()

    private val listener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            _messageFlow.value = text
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Timber.e(t, "WebSocket failure")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Timber.d("WebSocket closed: $code - $reason")
        }
    }

    fun connect(path: String) {
        val request = Request.Builder()
            .url(baseUrl + path)
            .build()
        
        webSocket = client.newWebSocket(request, listener)
    }

    fun send(message: String): Boolean {
        return webSocket?.send(message) ?: false
    }

    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }
}