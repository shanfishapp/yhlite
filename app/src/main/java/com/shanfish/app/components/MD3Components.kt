package com.example.mymd3app.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MD3Components(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(true) }
    var radioOption by remember { mutableStateOf("Option1") }
    var sliderValue by remember { mutableStateOf(0f) }
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. 文本组件
        Text("文本样式", style = MaterialTheme.typography.displayLarge)
        Text("Display Large", style = MaterialTheme.typography.displayLarge)
        Text("Display Medium", style = MaterialTheme.typography.displayMedium)
        Text("Display Small", style = MaterialTheme.typography.displaySmall)
        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
        Text("Headline Small", style = MaterialTheme.typography.headlineSmall)
        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
        Text("Body Small", style = MaterialTheme.typography.bodySmall)
        
        Divider()
        
        // 2. 按钮组件
        Text("按钮组件", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = {}) { Text("普通按钮") }
        ElevatedButton(onClick = {}) { Text("悬浮按钮") }
        FilledTonalButton(onClick = {}) { Text("填充色调按钮") }
        OutlinedButton(onClick = {}) { Text("轮廓按钮") }
        TextButton(onClick = {}) { Text("文本按钮") }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            FloatingActionButton(onClick = {}, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Share, contentDescription = "分享")
            }
            Spacer(modifier = Modifier.width(8.dp))
            SmallFloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Share, contentDescription = "小FAB")
            }
            Spacer(modifier = Modifier.width(8.dp))
            LargeFloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Share, contentDescription = "大FAB")
            }
        }
        
        Divider()
        
        // 3. 输入组件
        Text("输入组件", style = MaterialTheme.typography.headlineMedium)
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("文本输入框") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("轮廓文本输入框") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checked, onCheckedChange = { checked = it })
            Text("复选框", modifier = Modifier.padding(start = 8.dp))
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = radioOption == "Option1",
                onClick = { radioOption = "Option1" }
            )
            Text("选项1", modifier = Modifier.padding(start = 8.dp))
            
            RadioButton(
                selected = radioOption == "Option2",
                onClick = { radioOption = "Option2" }
            )
            Text("选项2", modifier = Modifier.padding(start = 8.dp))
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = checked, onCheckedChange = { checked = it })
            Text("开关", modifier = Modifier.padding(start = 8.dp))
        }
        
        Text("滑块: $sliderValue")
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..100f
        )
        
        Divider()
        
        // 4. 卡片组件
        Text("卡片组件", style = MaterialTheme.typography.headlineMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("普通卡片", style = MaterialTheme.typography.headlineSmall)
                Text("这是普通卡片的内容")
            }
        }
        
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("悬浮卡片", style = MaterialTheme.typography.headlineSmall)
                Text("这是悬浮卡片的内容")
            }
        }
        
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("轮廓卡片", style = MaterialTheme.typography.headlineSmall)
                Text("这是轮廓卡片的内容")
            }
        }
        
        Divider()
        
        // 5. 芯片组件
        Text("芯片组件", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text("辅助芯片") }
            )
            SuggestionChip(
                onClick = {},
                label = { Text("建议芯片") }
            )
            FilterChip(
                selected = checked,
                onClick = { checked = !checked },
                label = { Text("筛选芯片") }
            )
        }
        
        Divider()
        
        // 6. 顶部应用栏
        Text("应用栏组件", style = MaterialTheme.typography.headlineMedium)
        TopAppBar(
            title = { Text("顶部应用栏") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                }
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "更多")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("设置") },
                        onClick = { expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("关于") },
                        onClick = { expanded = false }
                    )
                }
            }
        )
        
        // 7. 底部导航
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Menu, contentDescription = "首页") },
                label = { Text("首页") },
                selected = selectedItem == 0,
                onClick = { selectedItem = 0 }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, contentDescription = "收藏") },
                label = { Text("收藏") },
                selected = selectedItem == 1,
                onClick = { selectedItem = 1 }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "设置") },
                label = { Text("设置") },
                selected = selectedItem == 2,
                onClick = { selectedItem = 2 }
            )
        }
        
        // 8. 底部应用栏
        BottomAppBar(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Favorite, contentDescription = "收藏")
            }
            Spacer(modifier = Modifier.weight(1f, true))
            IconButton(onClick = {}) {
                Icon(Icons.Default.Share, contentDescription = "分享")
            }
        }
        
        // 9. 密码输入框
        Text("密码输入框", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MD3ComponentsPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MD3Components()
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MD3ComponentsDarkPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MD3Components()
        }
    }
}