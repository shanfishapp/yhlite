package com.shanfish.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShanFishAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("选项1") }
    var sliderValue by remember { mutableStateOf(0f) }
    var checked by remember { mutableStateOf(false) }
    var switchChecked by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("未选择") }
    var selectedTime by remember { mutableStateOf("未选择") }

    val options = listOf("选项1", "选项2", "选项3", "选项4")

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("底部弹窗", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showBottomSheet = false }) {
                    Text("关闭")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("弹窗标题") },
            text = { Text("这是弹窗内容，可以包含更多详细信息") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    if (showDatePicker) {
        Dialog(onDismissRequest = { showDatePicker = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("选择日期", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showDatePicker = false }) {
                        Text("确认")
                    }
                }
            }
        }
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("选择时间", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showTimePicker = false }) {
                        Text("确认")
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ShanFishApp - MD3 组件预览") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("按钮组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text("Filled 按钮")
                    }
                    OutlinedButton(onClick = { showBottomSheet = true }) {
                        Text("Outlined 按钮")
                    }
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Text 按钮")
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "收藏")
                    }
                    IconButton(onClick = { /*TODO*/ }, enabled = false) {
                        Icon(Icons.Default.Share, contentDescription = "分享")
                    }
                    FilledIconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Add, contentDescription = "添加")
                    }
                    OutlinedIconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                }
            }

            item {
                Text("输入组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("标准文本框") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("带图标文本框") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Clear, contentDescription = "清除")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = { Text("轮廓文本框") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                var password by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码框") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("选择组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                    Text("复选框")

                    Switch(
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it }
                    )
                    Text("开关")
                }
            }

            item {
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedOption)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "下拉")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                Text("滑动组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("滑动值: ${sliderValue.toInt()}")
            }

            item {
                Text("进度组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                LinearProgressIndicator(
                    progress = 0.7f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    CircularProgressIndicator(progress = 0.7f)
                }
            }

            item {
                Text("卡片组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("标准卡片", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("这是一个标准的 Material Design 3 卡片组件")
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("浮起卡片", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("这是一个带有阴影效果的浮起卡片")
                    }
                }
            }

            item {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("轮廓卡片", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("这是一个带有轮廓线的卡片")
                    }
                }
            }

            item {
                Text("列表组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                ListItem(
                    headlineContent = { Text("列表项 1") },
                    leadingContent = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("列表项 2") },
                    supportingContent = { Text("支持文本") },
                    leadingContent = {
                        Icon(Icons.Default.Face, contentDescription = null)
                    },
                    trailingContent = {
                        Switch(checked = true, onCheckedChange = {})
                    }
                )
            }

            item {
                Text("徽章组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text("99+")
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "通知")
                    }

                    BadgedBox(
                        badge = {
                            Badge()
                        }
                    ) {
                        Icon(Icons.Default.Email, contentDescription = "邮件")
                    }
                }
            }

            item {
                Text("导航组件", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { /*TODO*/ },
                        icon = {
                            Icon(Icons.Default.Home, contentDescription = "首页")
                        },
                        label = { Text("首页") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { /*TODO*/ },
                        icon = {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        },
                        label = { Text("搜索") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { /*TODO*/ },
                        icon = {
                            Icon(Icons.Default.Person, contentDescription = "我的")
                        },
                        label = { Text("我的") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun ShanFishAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}