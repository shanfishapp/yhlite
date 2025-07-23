package com.shanfishapp.yhlite.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("platform") val platform: String
)