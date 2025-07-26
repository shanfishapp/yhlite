package com.shanfish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shanfish.app.components.ScreenManager as Nav
import com.shanfish.app.ui.screens.LoginScreen
import com.shanfish.app.ui.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 精确注册屏幕（使用明确ID）
        Nav.register("login") { 
            LoginScreen(
                onLoginClick = { Nav.jumpTo("message") },
                onRegisterClick = { Nav.jumpTo("register") }
            )
        }
        
        Nav.register("register") { 
            RegisterScreen(
                onBackToLogin = { Nav.jumpTo("login") }
            )
        }

        Nav.register("message") {
            MessagePlaceholderScreen(
                onLogout = { Nav.jumpTo("login") }
            )
        }

        // 设置初始路由
        Nav.jumpTo("login")

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Nav.getCurrentScreen()()
                }
            }
        }
    }
}

// 仅实现消息占位界面（其他屏幕从外部导入）
@Composable
private fun MessagePlaceholderScreen(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "消息界面占位符",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout) {
            Text("退出登录")
        }
    }
}

// 主题包装器
private val AppTheme = MaterialTheme {
    // 可在此配置自定义主题
}