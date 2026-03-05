package com.example.skillshare

import MainScreen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.*
import com.example.skillshare.ui.auth.*
import com.example.skillshare.ui.exchange.ExchangeRepository
import com.example.skillshare.ui.exchange.ExchangeViewModel
import com.example.skillshare.ui.exchange.ExchangeViewModelFactory

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // База данных
    val database = remember {
        AppDatabase.getDatabase(context)
    }

    // Repositories
    val adsRepository = remember { AdsRepository(database.adDao()) }
    val exchangeRepository = remember { ExchangeRepository(database.exchangeDao()) }

    // ViewModels
    val adsViewModel: AdsViewModel = viewModel(
        factory = AdsViewModelFactory(adsRepository)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(database.userDao())
    )

    val exchangeViewModel: ExchangeViewModel = viewModel(
        factory = ExchangeViewModelFactory(exchangeRepository)
    )

    // 🔥 Следим за текущим пользователем
    val currentUser by authViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUser?.id) {
        adsViewModel.setCurrentUser(currentUser?.id)
    }

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
                onRegisterClick = { navController.navigate("register") }
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
                onBack = { navController.popBackStack() }
            )
        }

        // MAIN с нижней навигацией
        composable("main") {
            MainScreen(
                navController = navController,
                adsViewModel = adsViewModel,
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        // CREATE AD
        composable("createAd") {
            val user = currentUser
            CreateAdScreen(
                currentUserId = user?.id ?: 0L,
                currentUsername = user?.username ?: "",
                onAdCreated = { ad ->
                    adsViewModel.addAd(ad)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // EDIT AD
        composable("editAd/{adId}") { backStackEntry ->
            val adId = backStackEntry.arguments?.getString("adId")?.toLongOrNull() ?: 0L
            LaunchedEffect(adId) { adsViewModel.loadAd(adId) }

            EditAdScreen(
                viewModel = adsViewModel,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // AD DETAILS
        composable("adDetails/{adId}") { backStackEntry ->
            val adId = backStackEntry.arguments?.getString("adId")?.toLongOrNull() ?: 0L
            LaunchedEffect(adId) { adsViewModel.loadAd(adId) }

            AdDetailsScreen(
                adsViewModel = adsViewModel,
                authViewModel = authViewModel,
                exchangeViewModel = exchangeViewModel, // ✅ Передаём сюда
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("editAd/$adId") }
            )
        }
    }
}