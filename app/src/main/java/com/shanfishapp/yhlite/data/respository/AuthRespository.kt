package com.shanfishapp.yhlite.data.repository

import com.shanfishapp.yhlite.data.model.LoginRequest
import com.shanfishapp.yhlite.data.model.LoginResponse
import com.shanfishapp.yhlite.data.network.RetrofitClient

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String, deviceId: String): Result<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(
                email = email,
                password = password,
                deviceId = deviceId,
                platform = "android"
            )
            val response = apiService.emailLogin(loginRequest)
            
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}