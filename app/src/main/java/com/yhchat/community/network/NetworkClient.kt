package com.yhchat.community.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import java.io.IOException

class NetworkClient(private val baseUrl: String) {
    private val client: OkHttpClient
    
    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun newRequest(path: String): Request.Builder {
        return Request.Builder()
            .url(baseUrl + path)
    }
    
    // HTTP请求方法
    @Throws(IOException::class)
    fun doRequest(request: Request): Response {
        return client.newCall(request).execute()
    }
    
    // 处理ZIP响应
    fun handleZipResponse(response: Response): ByteArray {
        return response.body?.bytes() ?: ByteArray(0)
    }
}