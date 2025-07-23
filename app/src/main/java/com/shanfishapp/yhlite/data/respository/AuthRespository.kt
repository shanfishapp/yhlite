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
            
            println("Sending login request: $loginRequest")
            
            val response = apiService.emailLogin(loginRequest)
            
            println("Received response: code=${response.code()}, isSuccessful=${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val loginResponse = response.body()
                println("Response body: $loginResponse")
                
                if (loginResponse != null) {
                    // 保存token到本地
                    loginResponse.data?.token?.let { token ->
                        tokenManager.saveToken(token)
                        println("Token saved: $token")
                    }
                    Result.success(loginResponse)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                println("Error body: $errorBody")
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}, Error: $errorBody"))
            }
        } catch (e: Exception) {
            println("Exception occurred: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
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