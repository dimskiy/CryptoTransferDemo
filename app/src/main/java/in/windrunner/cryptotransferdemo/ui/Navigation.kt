package `in`.windrunner.cryptotransferdemo.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import `in`.windrunner.cryptotransferdemo.ui.currency_selector.CurrenciesSelectorScreen
import `in`.windrunner.cryptotransferdemo.ui.transfer_screen.TransferScreen

sealed class NavScreen(val route: String) {
    data object Transfer : NavScreen("transfer_screen")
    data object CurrencySelector : NavScreen("currency_selector")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(startDestination: NavScreen, onError: (Throwable) -> Unit, modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination.route) {
        composable(route = NavScreen.Transfer.route) {
            TransferScreen(
                onErrorDisplay = onError,
                modifier = modifier,
                navController = navController
            )
        }

        dialog(route = NavScreen.CurrencySelector.route) {
            CurrenciesSelectorScreen(
                onError = onError,
                navController = navController,
                modifier = modifier
            )
        }
    }
}