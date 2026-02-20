package com.example.skillshare

import MainScreen
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.*
import com.example.skillshare.ui.auth.*

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // База
    val database = remember {
        AppDatabase.getDatabase(context)
    }

    // Repository
    val repository = remember {
        AdsRepository(database.adDao())
    }

    // ViewModels
    val adsViewModel: AdsViewModel = viewModel(
        factory = AdsViewModelFactory(repository)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(database.userDao())
    )

    // 🔥 Следим за текущим пользователем
    val currentUser by authViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "login" else "main"
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // REGISTER
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 🔥 MAIN (с нижней навигацией)
        composable("main") {
            MainScreen(
                adsViewModel = adsViewModel,
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        // Эти экраны можно оставить глобальными
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

        composable("editAd/{adId}") { backStackEntry ->

            val adId = backStackEntry.arguments
                ?.getString("adId")
                ?.toLongOrNull() ?: 0L

            LaunchedEffect(adId) {
                adsViewModel.loadAd(adId)
            }

            EditAdScreen(
                viewModel = adsViewModel,
                onSave = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}