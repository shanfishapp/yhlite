package com.yhchat.community.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yhchat.community.viewmodels.LoginViewModel
import com.yhchat.community.viewmodels.LoginResult

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit, // 新增导航到注册的回调
    viewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showAgreementDialog by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    // 使用 collectAsState 获取 ViewModel 中的状态
    val isLoading by viewModel.isLoading.collectAsState()
    val agreementAccepted by viewModel.agreementAccepted.collectAsState()

    LaunchedEffect(viewModel.loginResult) {
        viewModel.loginResult.collect { result ->
            when (result) {
                is LoginResult.Success -> onLoginSuccess()
                is LoginResult.Error -> errorMessage = result.message
                null -> { /* 初始状态不处理 */ }
            }
        }
    }

    if (showAgreementDialog) {
        AlertDialog(
            onDismissRequest = { showAgreementDialog = false },
            title = { Text("用户协议和隐私政策") },
            text = {
                Column {
                    Text(
                        "感谢您使用云湖社区！我们非常重视您的个人信息和隐私保护。" +
                        "为了更好地保障您的权益，请在使用我们的服务之前，仔细阅读并同意以下协议：",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        "1. 《用户服务协议》\n" +
                        "2. 《隐私政策》\n" +
                        "3. 《儿童隐私保护声明》",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setAgreementAccepted(true)
                        showAgreementDialog = false
                    }
                ) {
                    Text("同意并继续")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAgreementDialog = false }
                ) {
                    Text("暂不使用")
                }
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "登录",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    errorMessage = null
                },
                label = { Text("邮箱") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    errorMessage = null
                },
                label = { Text("密码") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "隐藏密码" else "显示密码"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = agreementAccepted,
                    onCheckedChange = { checked ->
                        if (!checked) {
                            viewModel.setAgreementAccepted(false)
                        } else {
                            showAgreementDialog = true
                        }
                    }
                )
                Text(
                    "我已阅读并同意《用户协议》和《隐私政策》",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    errorMessage = null
                    viewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("登录")
                }
            }

            // 分割线
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray.copy(alpha = 0.5f)
                )
                Text(
                    text = "或者",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray.copy(alpha = 0.5f)
                )
            }

            // 注册按钮 - 修改点击事件
            OutlinedButton(
                onClick = onNavigateToRegister, // 使用传入的导航回调
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("注册")
            }

            // 忘记密码链接
            Text(
                text = "忘记密码？",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { /* TODO: 处理忘记密码 */ },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}