import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shanfish.app.ui.components.LoginScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // 使用你的应用主题
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val isLogin = remember { mutableStateOf(false) } // 控制登录状态的变量

    if (isLogin.value) {
        // 将来替换为消息界面
        PlaceholderMessageScreen(
            onLogout = { isLogin.value = false } // 提供退出登录的回调
        )
    } else {
        LoginScreen(
            onLoginClick = { 
                // 这里模拟登录成功
                isLogin.value = true 
                // 实际项目中这里应该调用认证逻辑
            }
        )
    }
}

@Composable
fun PlaceholderMessageScreen(onLogout: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "消息界面 (待实现)",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = onLogout) {
                Text("退出登录")
            }
        }
    }
}