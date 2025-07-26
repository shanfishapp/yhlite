package com.shanfish.app.ui.screens

import com.shanfish.app.components.ScreenManager as Nav
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// 复用LoginScreen中的DialogState
sealed class DialogState {
    class Eula(val onAccept: () -> Unit, val onReject: () -> Unit) : DialogState()
    class Message(val text: String, val onConfirm: () -> Unit = {}) : DialogState()
}

@Composable
fun RegisterScreen() {
    var phone by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var acceptEula by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf<DialogState?>(null) }

    LaunchedEffect(isCountingDown, countdown) {
        if (isCountingDown && countdown > 0) {
            delay(1000)
            countdown--
        } else {
            isCountingDown = false
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
            text = "注册",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("手机号") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("验证码") },
                modifier = Modifier.weight(1f)
            )
            
            Button(
                onClick = {
                    if (phone.isBlank()) {
                        dialogState = DialogState.Message("请输入手机号")
                    } else {
                        countdown = 60
                        isCountingDown = true
                    }
                },
                enabled = !isCountingDown
            ) {
                Text(if (isCountingDown) "${countdown}s后重试" else "获取验证码")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("昵称（可选）") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inviteCode,
            onValueChange = { inviteCode = it },
            label = { Text("邀请码（可选）") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptEula,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        dialogState = DialogState.Eula(
                            onAccept = { acceptEula = true },
                            onReject = { acceptEula = false }
                        )
                    } else {
                        acceptEula = false
                    }
                }
            )
            Text("我已阅读并同意用户协议和隐私政策")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    !acceptEula -> dialogState = DialogState.Message("请先同意用户协议")
                    phone.isBlank() -> dialogState = DialogState.Message("请输入手机号")
                    verificationCode.isBlank() -> dialogState = DialogState.Message("请输入验证码")
                    else -> dialogState = DialogState.Message("注册功能暂未开放")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("注册")
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Text("或", modifier = Modifier.padding(horizontal = 8.dp))
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = { Nav.jumpTo("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("返回登录")
        }
    }

    // 弹窗显示
    when (val state = dialogState) {
        is DialogState.Eula -> AlertDialog(
            onDismissRequest = { dialogState = null },
            title = { Text("用户协议") },
            text = { Text("请阅读并同意用户协议和隐私政策") },
            confirmButton = {
                Button(onClick = { 
                    state.onAccept()
                    dialogState = null
                }) { Text("同意") }
            },
            dismissButton = {
                OutlinedButton(onClick = { 
                    state.onReject()
                    dialogState = null
                }) { Text("拒绝") }
            }
        )
        is DialogState.Message -> AlertDialog(
            onDismissRequest = { dialogState = null },
            title = { Text("提示") },
            text = { Text(state.text) },
            confirmButton = {
                Button(onClick = { 
                    state.onConfirm()
                    dialogState = null
                }) { Text("确定") }
            }
        )
        null -> {}
    }
}