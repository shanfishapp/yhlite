package com.example.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    // 登录类型状态
    enum class LoginType { PHONE, EMAIL }
    var selectedTab by remember { mutableStateOf(LoginType.PHONE) }
    
    // 表单状态
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var acceptEula by remember { mutableStateOf(false) }
    
    // 验证码倒计时
    var countdown by remember { mutableStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }
    
    // 弹窗状态
    var dialogState by remember { mutableStateOf<DialogState?>(null) }
    
    // 视图模型状态
    val loginState by viewModel.loginState.observeAsState()

    // 处理验证码倒计时
    LaunchedEffect(isCountingDown, countdown) {
        if (isCountingDown && countdown > 0) {
            delay(1000)
            countdown--
        } else {
            isCountingDown = false
        }
    }

    // 处理登录结果
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginResult.Success -> {
                dialogState = DialogState.Message("登录成功")
                delay(1000)
                onLoginSuccess()
            }
            is LoginResult.Failure -> {
                dialogState = DialogState.Message((loginState as LoginResult.Failure).error)
            }
            else -> {}
        }
    }

    // 弹窗类型
    sealed class DialogState {
        data class Eula(
            val onAccept: () -> Unit,
            val onReject: () -> Unit
        ) : DialogState()
        
        data class Message(
            val message: String,
            val onConfirm: () -> Unit = {}
        ) : DialogState()
    }

    // 弹窗组件
    @Composable
    fun CommonDialog(state: DialogState) {
        AlertDialog(
            onDismissRequest = { dialogState = null },
            title = { 
                Text(
                    when(state) {
                        is DialogState.Eula -> "用户协议"
                        is DialogState.Message -> "提示"
                    }
                )
            },
            text = { 
                Text(
                    when(state) {
                        is DialogState.Eula -> "请阅读并同意用户协议和隐私政策"
                        is DialogState.Message -> (state as DialogState.Message).message
                    }
                )
            },
            confirmButton = {
                when(state) {
                    is DialogState.Eula -> {
                        Button(onClick = { 
                            state.onAccept()
                            dialogState = null
                        }) {
                            Text("同意")
                        }
                    }
                    is DialogState.Message -> {
                        Button(onClick = { 
                            state.onConfirm()
                            dialogState = null
                        }) {
                            Text("确定")
                        }
                    }
                }
            },
            dismissButton = {
                when(state) {
                    is DialogState.Eula -> {
                        OutlinedButton(onClick = { 
                            state.onReject()
                            dialogState = null
                        }) {
                            Text("拒绝")
                        }
                    }
                    is DialogState.Message -> {}
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
        // 标题
        Text(
            text = "欢迎登录",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // 登录方式选项卡
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
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

        // 动态表单
        when (selectedTab) {
            LoginType.PHONE -> PhoneLoginForm(
                phone = phone,
                onPhoneChange = { phone = it },
                verificationCode = verificationCode,
                onVerificationCodeChange = { verificationCode = it },
                isCountingDown = isCountingDown,
                countdown = countdown,
                onSendCode = {
                    if (phone.isBlank()) {
                        dialogState = DialogState.Message("请输入手机号")
                    } else if (!Regex("^1[3-9]\\d{9}$").matches(phone)) {
                        dialogState = DialogState.Message("请输入正确的手机号")
                    } else {
                        viewModel.sendVerificationCode(phone)
                        countdown = 60
                        isCountingDown = true
                    }
                }
            )
            
            LoginType.EMAIL -> EmailLoginForm(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 用户协议
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
            TextButton(onClick = {
                dialogState = DialogState.Eula(
                    onAccept = { acceptEula = true },
                    onReject = { acceptEula = false }
                )
            }) {
                Text("我已阅读并同意用户协议和隐私政策")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 登录按钮
        Button(
            onClick = {
                when {
                    !acceptEula -> {
                        dialogState = DialogState.Message("请先同意用户协议和隐私政策")
                    }
                    selectedTab == LoginType.PHONE -> {
                        if (phone.isBlank()) {
                            dialogState = DialogState.Message("请输入手机号")
                        } else if (verificationCode.isBlank()) {
                            dialogState = DialogState.Message("请输入验证码")
                        } else {
                            viewModel.loginWithPhone(phone, verificationCode)
                        }
                    }
                    selectedTab == LoginType.EMAIL -> {
                        if (email.isBlank()) {
                            dialogState = DialogState.Message("请输入邮箱")
                        } else if (password.isBlank()) {
                            dialogState = DialogState.Message("请输入密码")
                        } else {
                            viewModel.loginWithEmail(email, password)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("登录")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 其他登录选项
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                dialogState = DialogState.Message(
                    if (selectedTab == LoginType.PHONE) 
                        "手机号找回功能暂未开放" 
                    else 
                        "密码找回功能暂未开放"
                )
            }) {
                Text(if (selectedTab == LoginType.PHONE) "遇到问题？" else "忘记密码？")
            }
            
            TextButton(onClick = {
                dialogState = DialogState.Message("注册账号")
            }) {
                Text("注册账号")
            }
        }
    }

    // 显示弹窗
    dialogState?.let { state ->
        CommonDialog(state = state)
    }
}

@Composable
private fun PhoneLoginForm(
    phone: String,
    onPhoneChange: (String) -> Unit,
    verificationCode: String,
    onVerificationCodeChange: (String) -> Unit,
    isCountingDown: Boolean,
    countdown: Int,
    onSendCode: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 手机号输入
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("手机号") },
            placeholder = { Text("请输入11位手机号") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 验证码输入行
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = onVerificationCodeChange,
                label = { Text("验证码") },
                placeholder = { Text("请输入6位验证码") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            
            Button(
                onClick = onSendCode,
                enabled = !isCountingDown,
                modifier = Modifier.height(56.dp)
            ) {
                Text(if (isCountingDown) "${countdown}s后重试" else "获取验证码")
            }
        }
    }
}

@Composable
private fun EmailLoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 邮箱输入
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("邮箱") },
            placeholder = { Text("请输入邮箱地址") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 密码输入
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("密码") },
            placeholder = { Text("请输入密码") },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) 
                            Icons.Default.Visibility 
                        else 
                            Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "隐藏密码" else "显示密码"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ViewModel 示例（实际项目中单独放在ViewModel文件中）
class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginResult>()
    val loginState: LiveData<LoginResult> = _loginState

    fun loginWithPhone(phone: String, code: String) {
        // 实际项目中这里调用登录接口
        _loginState.value = LoginResult.Success
    }

    fun loginWithEmail(email: String, password: String) {
        // 实际项目中这里调用登录接口
        _loginState.value = LoginResult.Success
    }

    fun sendVerificationCode(phone: String) {
        // 实际项目中这里调用发送验证码接口
    }
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Failure(val error: String) : LoginResult()
}