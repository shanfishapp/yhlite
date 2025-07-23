package com.shanfishapp.yhlite.utils

import android.os.Build
import java.security.MessageDigest

object DeviceUtils {
    fun getBoardId(): String {
        val board = Build.BOARD
        val hardware = Build.HARDWARE
        val serial = Build.SERIAL
        
        val combined = "$board$hardware$serial"
        return md5(combined).take(16) // 生成16位MD5哈希作为设备ID
    }
    
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}