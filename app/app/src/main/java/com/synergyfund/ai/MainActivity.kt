package com.synergyfund.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.synergyfund.ai.data.SessionManager
import com.synergyfund.ai.data.SessionManager
import com.synergyfund.ai.ui.screens.ChatScreen
import com.synergyfund.ai.ui.screens.DashboardScreen
import com.synergyfund.ai.ui.screens.LoginScreen
import com.synergyfund.ai.ui.theme.SynergyFundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize session manager for DataStore-backed token persistence
        SessionManager.init(applicationContext)
        // Initialize session manager for DataStore-backed token persistence
        SessionManager.init(applicationContext)
        setContent {
            SynergyFundApp()
        }
    }
}

@Composable
fun SynergyFundApp() {
    SynergyFundTheme {
        Surface {
            val navController = rememberNavController()
            SynergyNavHost(navController = navController)
        }
    }
}

@Composable
fun SynergyNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("dashboard") {
            DashboardScreen(
                onOpenChat = { navController.navigate("chat") }
            )
        }
        composable("chat") {
            ChatScreen(onBack = { navController.popBackStack() })
        }
    }
}
