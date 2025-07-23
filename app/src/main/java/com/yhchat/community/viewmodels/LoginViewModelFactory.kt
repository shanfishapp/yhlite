package com.yhchat.community.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yhchat.community.security.SecureStorage

class LoginViewModelFactory(private val secureStorage: SecureStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(secureStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}