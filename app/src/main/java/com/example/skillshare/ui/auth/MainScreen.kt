import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillshare.ui.ads.AdsScreen
import com.example.skillshare.ui.ads.AdsViewModel
import com.example.skillshare.ui.auth.AuthViewModel
import com.example.skillshare.ui.auth.ProfileScreen
import com.example.skillshare.ui.exchange.IncomingExchangesScreen
import com.example.skillshare.ui.exchange.ExchangeViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    adsViewModel: AdsViewModel,
    authViewModel: AuthViewModel,
    exchangeViewModel: ExchangeViewModel,   // 👈 ДОБАВИЛИ
    onLogout: () -> Unit
) {

    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = { innerNavController.navigate("ads") },
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Объявления") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { innerNavController.navigate("exchanges") },
                    icon = { Icon(Icons.Default.SwapHoriz, null) },
                    label = { Text("Обмены") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { innerNavController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Профиль") }
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