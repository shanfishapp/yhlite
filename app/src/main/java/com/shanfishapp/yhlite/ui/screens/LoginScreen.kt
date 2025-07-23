package com.shanfishapp.yhlite.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.shanfishapp.yhlite.R
import com.shanfishapp.yhlite.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "欢迎登录",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it }
                )
                TermsText()
            }

            Button(
                onClick = {
                    when {
                        email.isBlank() -> viewModel.showSnackbar(context.getString(R.string.email_required))
                        password.isBlank() -> viewModel.showSnackbar(context.getString(R.string.password_required))
                        !agreed -> viewModel.showSnackbar(context.getString(R.string.agree_terms_required))
                        else -> viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginViewModel.LoginState.Loading
            ) {
                if (loginState is LoginViewModel.LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(stringResource(R.string.login))
            }
        }

        loginState.let { state ->
            when (state) {
                is LoginViewModel.LoginState.Success -> {
                    LaunchedEffect(Unit) {
                        viewModel.showLoginSuccessDialog()
                    }
                }
                is LoginViewModel.LoginState.Error -> {
                    LaunchedEffect(Unit) {
                        viewModel.showLoginErrorDialog(state.message)
                    }
                }
                else -> {}
            }
        }

        if (viewModel.showSuccessDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.hideLoginSuccessDialog() },
                title = { Text("登录成功") },
                text = { Text("欢迎使用聊天应用！") },
                confirmButton = {
                    TextButton(onClick = { viewModel.hideLoginSuccessDialog() }) {
                        Text("确定")
                    }
                }
            )
        }

        viewModel.errorMessage.value?.let { message ->
            if (viewModel.showErrorDialog.value) {
                AlertDialog(
                    onDismissRequest = { viewModel.hideErrorDialog() },
                    title = { Text("登录失败") },
                    text = { Text(message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.hideErrorDialog() }) {
                            Text("确定")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TermsText() {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        append(context.getString(R.string.agree_terms))
        append(" ")
        pushStringAnnotation(
            tag = "terms",
            annotation = "terms"
        )
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(context.getString(R.string.terms_of_service))
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations("terms", offset, offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("file:///android_asset/terms.html")
                        putExtra("title", "服务条款")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // 如果无法打开assets中的文件，可以使用WebViewActivity
                    }
                }
        }
    )
}