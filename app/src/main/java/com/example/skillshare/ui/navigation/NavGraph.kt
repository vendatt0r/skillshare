package com.example.skillshare

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.AdsScreen
import com.example.skillshare.ui.ads.AdsViewModel
import com.example.skillshare.ui.ads.CreateAdScreen
import com.example.skillshare.ui.auth.LoginScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    // 🔥 Создаём ViewModel ОДИН раз
    val adsViewModel: AdsViewModel = viewModel()

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
            AdsScreen(
                viewModel = adsViewModel,
                onCreateClick = {
                    navController.navigate("createAd")
                }
            )
        }

        composable("createAd") {
            CreateAdScreen(
                onAdCreated = { ad ->
                    adsViewModel.addAd(ad)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

