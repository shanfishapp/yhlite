// app/src/main/java/com/yhchat/community/ui/screens/mine/MineScreen.kt
package com.yhchat.community.ui.screens.mine

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yhchat.community.viewmodels.LoginViewModel
import com.yhchat.community.ui.navigation.Routes

@Composable
fun MineScreen(
    viewModel: LoginViewModel,
    navController: NavController,
    onLogout: () -> Unit = { viewModel.logout() }
) {
    // 观察退出登录后的导航状态
    LaunchedEffect(viewModel.shouldNavigateToLogin) {
        viewModel.shouldNavigateToLogin.collect { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigate(Routes.Login.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
                viewModel.resetNavigationState()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "个人中心",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        
        // 修改后的退出登录按钮
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // 与LoginScreen按钮相同高度
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F), // 红色背景
                contentColor = Color.White // 白色文字
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Text(
                text = "退出登录",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}