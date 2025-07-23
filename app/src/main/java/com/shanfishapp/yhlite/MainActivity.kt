package com.shanfishapp.yhlite

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shanfishapp.yhlite.ui.screen.LoginScreen
import com.shanfishapp.yhlite.ui.theme.ChatAppTheme
import com.shanfishapp.yhlite.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val loginViewModel = LoginViewModel(this)
        
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoginScreen(viewModel = loginViewModel)
                        
                        // 添加调试按钮
                        if (BuildConfig.DEBUG) {
                            Button(
                                onClick = {
                                    startActivity(Intent(this, com.shanfishapp.yhlite.ui.activity.LogActivity::class.java))
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                            ) {
                                Text("查看日志")
                            }
                        }
                    }
                }
            }
        }
    }
}