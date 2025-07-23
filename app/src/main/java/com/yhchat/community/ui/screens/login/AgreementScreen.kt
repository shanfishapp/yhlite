// app/src/main/java/com/yhchat/community/ui/screens/login/AgreementScreen.kt
package com.yhchat.community.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AgreementScreen(
    onAgree: () -> Unit,
    onDisagree: () -> Unit
) {
    var checked by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "用户协议",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "这里是详细的用户协议内容...\n请仔细阅读并同意协议后继续使用",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
                Text(
                    text = "我已阅读并同意用户协议",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Button(
                onClick = onAgree,
                enabled = checked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("同意并继续")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(
                onClick = onDisagree,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("不同意")
            }
        }
    }
}