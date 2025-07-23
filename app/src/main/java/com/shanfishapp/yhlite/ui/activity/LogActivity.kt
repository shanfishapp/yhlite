package com.shanfishapp.yhlite.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.ScrollView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class LogActivity : AppCompatActivity() {
    
    private lateinit var logTextView: TextView
    private lateinit var scrollView: ScrollView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        scrollView = ScrollView(this)
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        logTextView = TextView(this).apply {
            setTextIsSelectable(true)
        }
        
        val clearButton = Button(this).apply {
            text = "清空日志"
        }
        
        val refreshButton = Button(this).apply {
            text = "刷新日志"
        }
        
        layout.addView(refreshButton)
        layout.addView(clearButton)
        layout.addView(logTextView)
        scrollView.addView(layout)
        setContentView(scrollView)
        
        clearButton.setOnClickListener {
            logTextView.text = ""
        }
        
        refreshButton.setOnClickListener {
            loadLogcat()
        }
        
        loadLogcat()
    }
    
    private fun loadLogcat() {
        try {
            val process = Runtime.getRuntime().exec("logcat -d -v time *:E")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val log = StringBuilder()
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                if (line?.contains("shanfishapp") == true) {
                    log.append(line).append("\n")
                }
            }
            
            logTextView.text = log.toString()
            
            // 滚动到底部
            scrollView.post {
                scrollView.fullScroll(android.view.View.FOCUS_DOWN)
            }
            
        } catch (e: Exception) {
            logTextView.text = "读取日志失败: ${e.message}"
        }
    }
}