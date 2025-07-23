// app/src/main/java/com/yhchat/community/ui/navigation/BottomNavItem.kt
package com.yhchat.community.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Message : BottomNavItem("message", "消息", Icons.Default.Message)
    object Contacts : BottomNavItem("contacts", "通讯录", Icons.Default.Person)
    object Community : BottomNavItem("community", "社区", Icons.Default.Group)
    object Discover : BottomNavItem("discover", "推荐", Icons.Default.Explore)
    object Mine : BottomNavItem("mine", "我的", Icons.Default.AccountCircle)

    companion object {
        fun items() = listOf(
            Message,
            Contacts,
            Community,
            Discover,
            Mine
        )
    }
}