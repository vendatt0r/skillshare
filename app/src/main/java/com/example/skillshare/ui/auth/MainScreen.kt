import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
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

@Composable
fun MainScreen(
    adsViewModel: AdsViewModel,
    authViewModel: AuthViewModel,
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
                    onLogout = onLogout,
                    onCreateClick = { },
                    onEditClick = { }
                )
            }

            composable("profile") {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}