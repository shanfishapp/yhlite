package com.yhchat.community.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class RegisterViewModel : ViewModel() {

    // 注册加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 是否同意协议
    private val _agreementAccepted = MutableStateFlow(false)
    val agreementAccepted: StateFlow<Boolean> = _agreementAccepted

    /**
     * 设置用户是否同意协议
     */
    fun setAgreementAccepted(accepted: Boolean) {
        _agreementAccepted.update { accepted }
    }

    /**
     * 用户注册逻辑（模拟实现，跳过发送验证码）
     *
     * @param phoneNumber 手机号
     * @param verificationCode 验证码（当前未使用，直接跳过）
     * @param nickname 昵称
     * @param invitationCode 邀请码（当前未使用）
     * @param onSuccess 注册成功回调
     * @param onError 注册失败回调（带错误信息）
     */
    fun register(
        phoneNumber: String,
        verificationCode: String,  // 当前未使用，直接跳过验证码步骤
        nickname: String,
        invitationCode: String,    // 当前未使用
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // 1. 检查是否同意协议
        if (!_agreementAccepted.value) {
            onError("请先同意用户协议")
            return
        }

        // 2. 简单校验参数是否为空
        if (phoneNumber.isBlank()) {
            onError("手机号不能为空")
            return
        }

        if (nickname.isBlank()) {
            onError("昵称不能为空")
            return
        }

        // 3. 开始模拟注册流程
        _isLoading.update { true }  // 设置加载状态为 true

        // 使用 viewModelScope 启动协程，避免内存泄漏
        viewModelScope.launch {
            delay(1000)  // 模拟网络请求延迟 1 秒

            _isLoading.update { false }  // 请求完成，设置加载状态为 false

            // 4. 模拟注册成功，调用 onSuccess 回调
            onSuccess()
        }
    }
}
