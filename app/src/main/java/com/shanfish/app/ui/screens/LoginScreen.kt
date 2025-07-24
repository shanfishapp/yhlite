package com.shanfish.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.shanfish.app.ui.components.TextWithDividers

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var acceptEula by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("登录结果") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("确定")
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "登录",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("邮箱") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility
                                   else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "隐藏密码" else "显示密码"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptEula,
                onCheckedChange = { acceptEula = it }
            )
            Text("我已阅读并同意 使用协议 和 隐私协议", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!acceptEula) {
                    dialogMessage = "请同意软件使用协议与隐私协议"
                    showDialog = true
                } else if (email.isBlank() || password.isBlank()) {
                    dialogMessage = "邮箱或密码未填写，请填写完整"
                    showDialog = true
                } else {
                    dialogMessage = "登录成功，欢迎！"
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }
        Spacer(modifier = Modifier.height(8ldp))
        TextWithDividers("或者")
        Spacer(modifier = Modifier.height(8ldp))
        OutlinedButton(
            onClick = {
                dialogMessage = "注册 功能暂未开放"
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("注册")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            dialogMessage = "忘记密码 功能暂未开放"
            showDialog = true
        }) {
            Text("忘记密码？点我重置")
        }
    }
}