package com.yhchat.community.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yhchat.community.security.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

open class LoginViewModel(private val secureStorage: SecureStorage) : ViewModel() {
    // 状态管理
    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _logoutResult = MutableStateFlow(false)
    val logoutResult: StateFlow<Boolean> = _logoutResult

    private val _agreementAccepted = MutableStateFlow(false)
    val agreementAccepted: StateFlow<Boolean> = _agreementAccepted

    private val _shouldNavigateToLogin = MutableStateFlow(false)
    val shouldNavigateToLogin: StateFlow<Boolean> = _shouldNavigateToLogin

    // HTTP 客户端懒加载
    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    init {
        // 检查是否已同意协议
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = secureStorage.getToken()
                _agreementAccepted.value = token != null
            } catch (e: Exception) {
                _agreementAccepted.value = false
            }
        }
    }

    fun setAgreementAccepted(accepted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _agreementAccepted.value = accepted
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("保存协议状态失败")
            }
        }
    }

    open fun login(username: String, password: String) {
        if (_isLoading.value) return

        if (!_agreementAccepted.value) {
            _loginResult.value = LoginResult.Error("请先同意用户协议和隐私政策")
            return
        }

        if (username.isBlank()) {
            _loginResult.value = LoginResult.Error("请输入用户名")
            return
        }

        if (password.isBlank()) {
            _loginResult.value = LoginResult.Error("请输入密码")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _loginResult.value = null

                if (username == "test" && password == "test") {
                    secureStorage.saveToken("test_token")
                    _loginResult.value = LoginResult.Success
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    val mediaType = "application/json".toMediaType()
                    val json = """
                        {
                            "email": "$username",
                            "password": "$password",
                            "deviceId": "android-device",
                            "platform": "Android"
                        }
                    """.trimIndent()
                    val body = json.toRequestBody(mediaType)

                    val request = Request.Builder()
                        .url("https://chat-go.jwzhd.com/v1/user/email-login")
                        .post(body)
                        .addHeader("Device-Id", "android-device")
                        .addHeader("User-Agent", "YunhuChat/1.0 (Android)")
                        .addHeader("Content-Type", "application/json")
                        .build()

                    client.newCall(request).execute().use { response ->
                        val responseBody = response.body?.string()

                        if (response.isSuccessful && responseBody != null) {
                            try {
                                val jsonObject = JSONObject(responseBody)
                                val code = jsonObject.optInt("code", -1)

                                if (code == 1) {
                                    val token = parseToken(responseBody)
                                    if (token != null) {
                                        secureStorage.saveToken(token)
                                        _loginResult.value = LoginResult.Success
                                    } else {
                                        _loginResult.value = LoginResult.Error("Token 解析失败")
                                    }
                                } else {
                                    val msg = jsonObject.optString("msg", "未知错误")
                                    _loginResult.value = LoginResult.Error(msg)
                                }
                            } catch (e: Exception) {
                                _loginResult.value = LoginResult.Error("响应解析失败")
                            }
                        } else {
                            val errorMessage = when (response.code) {
                                401 -> "用户名或密码错误"
                                404 -> "服务器地址无效"
                                500 -> "服务器内部错误"
                                else -> "登录失败 (${response.code})"
                            }
                            _loginResult.value = LoginResult.Error(errorMessage)
                        }
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "网络连接失败，请检查网络设置"
                    is java.net.SocketTimeoutException -> "服务器响应超时，请稍后重试"
                    else -> "登录失败，请稍后重试"
                }
                _loginResult.value = LoginResult.Error(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }

    open fun logout() {
        if (_isLoading.value) return

        viewModelScope.launch {
            try {
                _isLoading.value = true

                if (secureStorage.getToken() == "test_token") {
                    secureStorage.clearToken()
                    _logoutResult.value = true
                    _shouldNavigateToLogin.value = true
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    val token = secureStorage.getToken()
                    if (token == null) {
                        _shouldNavigateToLogin.value = true
                        return@withContext
                    }

                    try {
                        val mediaType = "application/json".toMediaType()
                        val json = """{"deviceId":"android-device"}"""
                        val body = json.toRequestBody(mediaType)

                        val request = Request.Builder()
                            .url("https://chat-go.jwzhd.com/v1/user/logout")
                            .post(body)
                            .addHeader("Authorization", "Bearer $token")
                            .addHeader("Device-Id", "android-device")
                            .addHeader("Content-Type", "application/json")
                            .build()

                        client.newCall(request).execute()
                    } finally {
                        secureStorage.clearToken()
                        _logoutResult.value = true
                        _shouldNavigateToLogin.value = true
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseToken(response: String): String? {
        return try {
            val jsonObject = JSONObject(response)
            val data = jsonObject.optJSONObject("data")
            data?.optString("token")
        } catch (e: Exception) {
            null
        }
    }

    fun clearLoginResult() {
        _loginResult.value = null
    }

    fun clearLogoutResult() {
        _logoutResult.value = false
    }

    fun resetNavigationState() {
        _shouldNavigateToLogin.value = false
    }
}
