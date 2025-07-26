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

// 将枚举和密封类移到顶层
enum class LoginType { PHONE, EMAIL }

sealed class DialogState {
    class Eula(val onAccept: () -> Unit, val onReject: () -> Unit) : DialogState()
    class Message(val text: String, val onConfirm: () -> Unit = {}) : DialogState()
}

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(LoginType.PHONE) }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
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
            text = "登录",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == LoginType.PHONE,
                onClick = { selectedTab = LoginType.PHONE },
                text = { Text("手机登录") }
            )
            Tab(
                selected = selectedTab == LoginType.EMAIL,
                onClick = { selectedTab = LoginType.EMAIL },
                text = { Text("邮箱登录") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (selectedTab) {
            LoginType.PHONE -> {
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
            }
            
            LoginType.EMAIL -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("邮箱") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                                imageVector = if (isPasswordVisible) 
                                    Icons.Default.Visibility 
                                else 
                                    Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "隐藏密码" else "显示密码"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

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
                    selectedTab == LoginType.PHONE && phone.isBlank() -> 
                        dialogState = DialogState.Message("请输入手机号")
                    selectedTab == LoginType.PHONE && verificationCode.isBlank() -> 
                        dialogState = DialogState.Message("请输入验证码")
                    selectedTab == LoginType.EMAIL && email.isBlank() -> 
                        dialogState = DialogState.Message("请输入邮箱")
                    selectedTab == LoginType.EMAIL && password.isBlank() -> 
                        dialogState = DialogState.Message("请输入密码")
                    else -> onLoginClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Text("或", modifier = Modifier.padding(horizontal = 8.dp))
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = { Nav.jumpTo("register") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("注册")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(onClick = { 
            dialogState = DialogState.Message(
                if (selectedTab == LoginType.PHONE) "手机号找回功能暂未开放" 
                else "密码找回功能暂未开放"
            )
        }) {
            Text(if (selectedTab == LoginType.PHONE) "遇到问题？" else "忘记密码？")
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