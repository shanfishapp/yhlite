package com.yhchat.community.security

import android.content.Context

class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun setAgreementAccepted(accepted: Boolean) {
        prefs.edit().putBoolean("agreement_accepted", accepted).apply()
    }

    fun isAgreementAccepted(): Boolean {
        return prefs.getBoolean("agreement_accepted", false)
    }
}