package com.shanfishapp.yhlite.data.repository

import android.content.Context
import com.shanfishapp.yhlite.data.local.TokenManager
import com.shanfishapp.yhlite.data.model.LoginRequest
import com.shanfishapp.yhlite.data.model.LoginResponse
import com.shanfishapp.yhlite.data.network.RetrofitClient
import com.shanfishapp.yhlite.utils.DeviceUtils

class AuthRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val tokenManager = TokenManager(context)

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val deviceId = DeviceUtils.getBoardId()
            val loginRequest = LoginRequest(
                email = email,
                password = password,
                deviceId = deviceId,
                platform = "android"
            )
            val response = apiService.emailLogin(loginRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    // 保存token到本地
                    loginResponse.data?.token?.let { token ->
                        tokenManager.saveToken(token)
                    }
                    Result.success(loginResponse)
                } ?: run {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            if (e.message?.contains("JsonReader.setLenient") == true) {
                Result.failure(Exception("服务器返回的数据格式不正确"))
            } else {
                Result.failure(e)
            }
        }
    }
    
    fun getToken(): String? {
        return tokenManager.getToken()
    }
    
    fun hasToken(): Boolean {
        return tokenManager.hasToken()
    }
    
    fun clearToken() {
        tokenManager.clearToken()
    }
}