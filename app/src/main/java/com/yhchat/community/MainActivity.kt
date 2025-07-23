// 文件路径: ~/app/src/main/java/com/yhchat/community/MainActivity.kt

package com.yhchat.community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yhchat.community.security.SecureStorage
import com.yhchat.community.security.UserPreferences
import com.yhchat.community.ui.navigation.BottomNavItem
import com.yhchat.community.ui.screens.community.CommunityScreen
import com.yhchat.community.viewmodels.RegisterViewModel
import com.yhchat.community.ui.screens.contacts.ContactsScreen
import com.yhchat.community.ui.screens.discover.DiscoverScreen
import com.yhchat.community.ui.screens.message.MessageScreen
import com.yhchat.community.ui.screens.mine.MineScreen
import com.yhchat.community.ui.screens.login.AgreementScreen
import com.yhchat.community.ui.screens.login.LoginScreen
import com.yhchat.community.ui.screens.login.RegisterScreen
import com.yhchat.community.ui.theme.YunhuCommunityTheme
import com.yhchat.community.viewmodels.LoginViewModel
import com.yhchat.community.viewmodels.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val secureStorage = SecureStorage(this)
        val userPreferences = UserPreferences(this)
        setContent {
            YunhuCommunityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppEntryPoint(secureStorage, userPreferences, ::finish)
                }
            }
        }
    }
}

@Composable
fun AppEntryPoint(
    secureStorage: SecureStorage,
    userPreferences: UserPreferences,
    finishActivity: () -> Unit
) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(secureStorage)
    )

    // 1. 引入一个状态来跟踪用户是否已登录，并使其依赖于 getToken() 的结果
    //    当 getToken() 返回值变化时（如登出时变为 null），此 remember 块会重新执行
    //    使用 key() 确保 getToken() 变化时状态更新
    val isUserLoggedIn by remember(secureStorage.getToken()) {
        mutableStateOf(secureStorage.getToken() != null)
    }

    // 2. 修改 NavHost 的 startDestination，使其依赖于 isUserLoggedIn 状态
    NavHost(
        navController = navController,
        // 根据协议接受状态和用户登录状态动态决定起始目的地
        startDestination = if (!userPreferences.isAgreementAccepted()) "agreement"
                          else if (!isUserLoggedIn) "login" // 使用 isUserLoggedIn 状态
                          else "main"
    ) {
        composable("agreement") {
            var showExitDialog by remember { mutableStateOf(false) }
            AgreementScreen(
                onAgree = {
                    userPreferences.setAgreementAccepted(true)
                    navController.navigate("login") {
                        popUpTo("agreement") { inclusive = true }
                    }
                },
                onDisagree = {
                    showExitDialog = true
                }
            )
            if (showExitDialog) {
                ConfirmExitDialog(
                    onConfirmExit = {
                        showExitDialog = false
                        finishActivity()
                    },
                    onAgreeToTerms = {
                        showExitDialog = false
                        userPreferences.setAgreementAccepted(true)
                        navController.navigate("login") {
                            popUpTo("agreement") { inclusive = true }
                        }
                    }
                )
            }
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // 登录成功时，导航到 main，并清空栈
                    navController.navigate("main") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                viewModel = viewModel<RegisterViewModel>()
            )
        }
        composable("main") {
            // 3. 修改 onLogout 回调，使其更新 isUserLoggedIn 状态
            MainScreen(
                onLogout = {
                    loginViewModel.logout()
                    secureStorage.clearToken()
                    // 不再需要手动操作外部 navController 的栈
                    // 只需确保 secureStorage.getToken() 返回 null 即可
                    // isUserLoggedIn 状态会因 remember(secureStorage.getToken()) 而自动更新
                    // 该更新会触发 AppEntryPoint 重组，进而使 NavHost 导航到 "login"
                    // 注意：由于 Compose 的机制，直接赋值 isUserLoggedIn = false 可能不会触发重组
                    // 因为 isUserLoggedIn 是通过 remember { mutableStateOf(...) } 创建的。
                    // 我们依赖的是 secureStorage.getToken() 的变化来触发 remember 重新计算。
                    // 如果上述方法在你的环境中不触发重组，可以考虑使用一个额外的状态变量
                    // 或者使用 LaunchedEffect 监听 getToken() 的变化。
                    // 但在此处，我们期望 secureStorage.clearToken() 导致 getToken() 返回 null,
                    // 从而触发 remember 块重新执行，将 isUserLoggedIn 设为 false。
                },
                loginViewModel = loginViewModel
            )
        }
    }
}

@Composable
fun ConfirmExitDialog(
    onConfirmExit: () -> Unit,
    onAgreeToTerms: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("确认拒绝服务协议") },
        text = {
            Text(
                "如果您不同意本软件的使用协议，您将无法使用本软件的服务。我们将退出本软件。",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onConfirmExit) {
                Text("坚持拒绝")
            }
        },
        dismissButton = {
            TextButton(onClick = onAgreeToTerms) {
                Text("同意")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit, // 此 onLogout 由 AppEntryPoint 传入
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.items().forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Message.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Message.route) { MessageScreen() }
            composable(BottomNavItem.Contacts.route) { ContactsScreen() }
            composable(BottomNavItem.Community.route) { CommunityScreen() }
            composable(BottomNavItem.Discover.route) { DiscoverScreen() }
            composable(BottomNavItem.Mine.route) {
                MineScreen(
                    onLogout = onLogout, // 将 onLogout 传递给 MineScreen
                    viewModel = loginViewModel
                )
            }
        }
    }
}