package com.yhchat.community.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun TopBarActions(
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = { showMenu = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "更多选项"
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = { 
                    showMenu = false
                    // TODO: 处理群聊创建
                },
                text = { Text("发起群聊") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = null
                    )
                }
            )
            
            DropdownMenuItem(
                onClick = { 
                    showMenu = false
                    // TODO: 处理添加好友
                },
                text = { Text("添加好友") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null
                    )
                }
            )
            
            DropdownMenuItem(
                onClick = { 
                    showMenu = false
                    // TODO: 处理扫码功能
                },
                text = { Text("扫一扫") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null
                    )
                }
            )
        }
    }
}