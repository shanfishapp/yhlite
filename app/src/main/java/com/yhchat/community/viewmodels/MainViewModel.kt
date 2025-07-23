package com.yhchat.community.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var showBottomNav by mutableStateOf(true)
        private set

    fun toggleBottomNav() {
        showBottomNav = !showBottomNav
    }

    fun setBottomNavVisibility(visible: Boolean) {
        showBottomNav = visible
    }
}