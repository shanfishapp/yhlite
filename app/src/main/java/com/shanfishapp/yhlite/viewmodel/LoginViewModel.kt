package com.shanfishapp.yhlite.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shanfishapp.yhlite.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val context: Context) : ViewModel() {
    private val repository = AuthRepository(context)
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    val showSuccessDialog = mutableStateOf(false)
    val showErrorDialog = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val token: String?) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            repository.login(email, password)
                .onSuccess { response ->
                    if (response.code == 1) {
                        _loginState.value = LoginState.Success(response.data?.token)
                    } else {
                        _loginState.value = LoginState.Error("登录失败：${response.msg}(${response.code})")
                    }
                }
                .onFailure { exception ->
                    _loginState.value = LoginState.Error("网络错误: ${exception.message}")
                }
        }
    }

    fun showLoginSuccessDialog() {
        showSuccessDialog.value = true
    }

    fun hideLoginSuccessDialog() {
        showSuccessDialog.value = false
        _loginState.value = LoginState.Idle
    }

    fun showLoginErrorDialog(message: String) {
        errorMessage.value = message
        showErrorDialog.value = true
    }

    fun hideErrorDialog() {
        showErrorDialog.value = false
        errorMessage.value = null
        _loginState.value = LoginState.Idle
    }

    fun showSnackbar(message: String) {
        errorMessage.value = message
        showErrorDialog.value = true
    }
    
    fun hasToken(): Boolean {
        return repository.hasToken()
    }
}