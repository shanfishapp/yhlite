package com.yhchatlite.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var webViewProtocol: WebView

    private val client = OkHttpClient()
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        setupWebView()
        setupLoginButton()
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        webViewProtocol = findViewById(R.id.webViewProtocol)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webViewProtocol.settings.javaScriptEnabled = true
        webViewProtocol.loadUrl("file:///android_asset/protocol.html")
    }

    private fun setupLoginButton() {
        buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入邮箱和密码", Toast.LENGTH_SHORT).show()
            return
        }

        val deviceId = DeviceIdManager.getDeviceId(this)
        val url = "https://chat-go.jwzhd.com/v1/user/email-login "

        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password", password)
            put("deviceId", deviceId)
            put("platform", "android")
        }.toString()

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toRequestBody(jsonType))
            .header("user-agent", "android 1.4.80")
            .header("content-type", "application/json")
            .header("accept-encoding", "gzip")
            .header("token", "") // 空即可
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute().use { it }
                val responseBody = response.body?.string() ?: "{}"

                runOnUiThread {
                    handleResponse(responseBody)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    showErrorDialog("网络错误", e.message ?: "请求失败")
                }
            }
        }.start()
    }

    private fun handleResponse(responseBody: String) {
        try {
            val json = JSONObject(responseBody)
            val code = json.getInt("code")
            val msg = json.optString("msg", "未知错误")

            if (code == 1) {
                val token = json.getJSONObject("data").getString("token")
                TokenManager.saveToken(this, token)
                Toast.makeText(this, "登录成功！", Toast.LENGTH_LONG).show()
            } else {
                showErrorDialog(code.toString(), msg)
            }
        } catch (e: Exception) {
            showErrorDialog("解析错误", "响应格式异常：$e")
        }
    }

    private fun showErrorDialog(code: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle("登录失败")
            .setMessage("错误码：$code\n原因：$message")
            .setPositiveButton("确定", null)
            .show()
    }
}