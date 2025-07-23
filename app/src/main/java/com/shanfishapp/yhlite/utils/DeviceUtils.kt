package com.shanfishapp.yhlite.utils

import android.os.Build
import java.security.MessageDigest
import java.util.Locale

object DeviceUtils {
    fun getBoardId(): String {
        val board = Build.BOARD ?: ""
        val hardware = Build.HARDWARE ?: ""
        val serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            @Suppress("DEPRECATION")
            Build.SERIAL
        }
        
        val combined = "$board$hardware$serial".lowercase(Locale.getDefault())
        return if (combined.isNotEmpty()) {
            md5(combined).take(16)
        } else {
            // 如果获取不到硬件信息，生成随机ID
            generateRandomId()
        }
    }
    
    private fun md5(input: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(input.toByteArray())
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            generateRandomId()
        }
    }
    
    private fun generateRandomId(): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
        return (1..16)
            .map { chars.random() }
            .joinToString("")
    }
}