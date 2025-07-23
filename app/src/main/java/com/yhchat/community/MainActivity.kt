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

    NavHost(
        navController = navController,
        startDestination = if (!userPreferences.isAgreementAccepted()) "agreement" 
                         else if (secureStorage.getToken() == null) "login"
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
            MainScreen(
                onLogout = {
                    loginViewModel.logout()
                    secureStorage.clearToken()
                    // 最彻底的方法：清空所有回退栈
                    navController.popBackStack(navController.graph.startDestinationId, true)
                    navController.navigate("login")
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
    onLogout: () -> Unit,
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
                    onLogout = onLogout,
                    viewModel = loginViewModel
                ) 
            }
        }
    }
}