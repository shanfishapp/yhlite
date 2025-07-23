package com.shanfishapp.yhlite.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: LoginData?
)

data class LoginData(
    @SerializedName("token") val token: String?,
    @SerializedName("userId") val userId: String?
)