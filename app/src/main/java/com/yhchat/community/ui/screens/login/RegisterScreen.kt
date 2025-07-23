package com.yhchat.community.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yhchat.community.viewmodels.RegisterViewModel

@Composable  
fun RegisterScreen(  
    onRegisterSuccess: () -> Unit,  
    onNavigateToLogin: () -> Unit, // 新增返回登录的回调  
    viewModel: RegisterViewModel  
) {
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var invitationCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSendingCode by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val agreementAccepted by viewModel.agreementAccepted.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "注册",
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

            // 手机号输入框
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("手机号") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 验证码输入框（保留）
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text("验证码") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        // TODO: 替换为发送验证码逻辑
                        isSendingCode = true
                        errorMessage = "发送验证码功能暂未实现"
                        isSendingCode = false
                    },
                    enabled = phoneNumber.isNotBlank() && !isSendingCode
                ) {
                    Text(if (isSendingCode) "发送中..." else "发送验证码")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 昵称输入框
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("昵称 (可选)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 邀请码输入框
            OutlinedTextField(
                value = invitationCode,
                onValueChange = { invitationCode = it },
                label = { Text("邀请码 (可选)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 协议勾选
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = agreementAccepted,
                    onCheckedChange = { checked ->
                        viewModel.setAgreementAccepted(checked)
                    }
                )
                Text(
                    text = "我已阅读并同意《用户协议》和《隐私政策》",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 注册按钮（禁止注册）
            Button(
                onClick = {
                    errorMessage = "注册功能暂未开放" // 直接显示禁止提示
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
                    Text("注册")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))  
  
            TextButton(  
                onClick = onNavigateToLogin,  
                modifier = Modifier.fillMaxWidth()  
            ) {  
                Text("已有账号？返回登录")  
            }
        }
    }
}