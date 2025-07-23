package com.shanfishapp.yhlite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // 加载 assets 目录下的 terms.html 文件
        webView.loadUrl("file:///android_asset/terms.html")

        setContentView(webView)
    }
}