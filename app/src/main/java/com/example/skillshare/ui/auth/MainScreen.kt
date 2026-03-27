import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.skillshare.ui.ads.AdsScreen
import com.example.skillshare.ui.ads.AdsViewModel
import com.example.skillshare.ui.auth.AuthViewModel
import com.example.skillshare.ui.auth.ProfileScreen
import com.example.skillshare.ui.exchange.ExchangeViewModel
import com.example.skillshare.ui.exchange.IncomingExchangesScreen

@Composable
fun MainScreen(
    navController: NavHostController,
    adsViewModel: AdsViewModel,
    authViewModel: AuthViewModel,
    exchangeViewModel: ExchangeViewModel,
    onLogout: () -> Unit
) {

    val innerNavController = rememberNavController()

    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color(0xFF070A13),
        bottomBar = {

            NavigationBar(containerColor = Color(0xFF070A13)) {

                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00E676),
                    selectedTextColor = Color(0xFF00E676),
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent // 👈 убрали белый овал
                )

                NavigationBarItem(
                    selected = currentRoute == "ads",
                    onClick = { innerNavController.navigate("ads") },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Объявления") },
                    colors = itemColors
                )

                NavigationBarItem(
                    selected = currentRoute == "exchanges",
                    onClick = { innerNavController.navigate("exchanges") },
                    icon = { Icon(Icons.Default.SwapHoriz, contentDescription = null) },
                    label = { Text("Обмены") },
                    colors = itemColors
                )

                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = { innerNavController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Профиль") },
                    colors = itemColors
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = innerNavController,
            startDestination = "ads",
            modifier = Modifier.padding(padding)
        ) {

            composable("ads") {
                AdsScreen(
                    viewModel = adsViewModel,
                    authViewModel = authViewModel,
                    onCreateClick = {
                        navController.navigate("createAd")
                    },
                    onAdClick = { adId ->
                        navController.navigate("adDetails/$adId")
                    }
                )
            }

            composable("exchanges") {
                IncomingExchangesScreen(
                    exchangeViewModel = exchangeViewModel,
                    authViewModel = authViewModel,
                    onOpenChat = { exchangeId ->
                        navController.navigate("chat/$exchangeId")
                    }
                )
            }

            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    adsViewModel = adsViewModel,
                    onLogout = onLogout,
                    onEdit = { adId ->
                        navController.navigate("editAd/$adId")
                    }
                )
            }
        }
    }
}