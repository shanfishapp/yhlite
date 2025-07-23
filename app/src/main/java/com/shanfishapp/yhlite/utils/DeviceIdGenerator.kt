package com.shanfishapp.yhlite.utils

import java.util.Random

object DeviceIdGenerator {
    private const val CHARS = "abcdefghijklmnopqrstuvwxyz0123456789"
    
    fun generateDeviceId(length: Int = 16): String {
        val random = Random()
        return (1..length)
            .map { CHARS[random.nextInt(CHARS.length)] }
            .joinToString("")
    }
}