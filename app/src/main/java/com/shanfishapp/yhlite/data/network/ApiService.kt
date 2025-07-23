package com.shanfishapp.yhlite.data.network

import com.shanfishapp.yhlite.data.model.LoginRequest
import com.shanfishapp.yhlite.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/user/email-login")
    suspend fun emailLogin(
        @Body loginRequest: LoginRequest,
        @Header("token") token: String = ""
    ): Response<LoginResponse>
}