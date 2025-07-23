package com.yhchat.community.ui.screens.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen() {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 头像
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF800080))
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "test",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "ID: 12345",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "菜单")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("添加好友") },
                            leadingIcon = { 
                                Icon(Icons.Default.PersonAdd, contentDescription = null)
                            },
                            onClick = { 
                                showMenu = false
                                // TODO: 实现添加好友功能
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("创建群聊") },
                            leadingIcon = { 
                                Icon(Icons.Default.GroupAdd, contentDescription = null)
                            },
                            onClick = { 
                                showMenu = false
                                // TODO: 实现群聊创建功能
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("扫一扫") },
                            leadingIcon = { 
                                Icon(Icons.Default.QrCode, contentDescription = null)
                            },
                            onClick = { 
                                showMenu = false
                                // TODO: 实现扫码功能
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            // TODO: 这里将添加消息列表
            Text("消息列表")
        }
    }
}