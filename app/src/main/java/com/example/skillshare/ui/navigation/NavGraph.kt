package com.example.skillshare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.*
import com.example.skillshare.ui.auth.LoginScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // 🔥 Создаём базу
    val database = remember {
        AppDatabase.getDatabase(context)
    }

    // 🔥 Создаём repository
    val repository = remember {
        AdsRepository(database.adDao())
    }

    // 🔥 Создаём ViewModel через factory
    val adsViewModel: AdsViewModel = viewModel(
        factory = AdsViewModelFactory(repository)
    )

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
                },
                onEditClick = { adId ->
                    navController.navigate("editAd/$adId")
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

        composable("editAd/{adId}") { backStackEntry ->

            val adId = backStackEntry.arguments?.getString("adId")?.toLongOrNull() ?: 0L

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
