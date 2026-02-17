package com.example.skillshare

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.AdsScreen
import com.example.skillshare.ui.auth.LoginScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("ads") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("ads") {
            AdsScreen()
        }
    }
}
