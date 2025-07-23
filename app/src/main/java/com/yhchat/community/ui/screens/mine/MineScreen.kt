// app/src/main/java/com/yhchat/community/ui/screens/mine/MineScreen.kt
package com.yhchat.community.ui.screens.mine

import androidx.compose.foundation.layout.*
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
        )
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
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