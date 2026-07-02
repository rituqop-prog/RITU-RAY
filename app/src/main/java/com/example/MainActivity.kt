package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.AppDatabase
import com.example.ui.AdminDashboardRoute
import com.example.ui.AdminDashboardScreen
import com.example.ui.LoginRoute
import com.example.ui.LoginScreen
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.ManagePaymentRoute
import com.example.ui.ManagePaymentScreen
import com.example.ui.TournamentDetailRoute
import com.example.ui.TournamentDetailScreen
import com.example.ui.UserDashboardRoute
import com.example.ui.UserDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(database.appDao()))

                    NavHost(navController = navController, startDestination = LoginRoute) {
                        composable<LoginRoute> {
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = { isAdmin ->
                                    if (isAdmin) {
                                        navController.navigate(AdminDashboardRoute) {
                                            popUpTo(LoginRoute) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(UserDashboardRoute) {
                                            popUpTo(LoginRoute) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        composable<AdminDashboardRoute> {
                            AdminDashboardScreen(
                                viewModel = viewModel,
                                onNavigateToTournament = { id ->
                                    navController.navigate(TournamentDetailRoute(id))
                                },
                                onNavigateToPaymentSettings = {
                                    navController.navigate(ManagePaymentRoute)
                                },
                                onLogout = {
                                    navController.navigate(LoginRoute) {
                                        popUpTo(0) // Clear back stack
                                    }
                                }
                            )
                        }

                        composable<UserDashboardRoute> {
                            UserDashboardScreen(
                                viewModel = viewModel,
                                onNavigateToTournament = { id ->
                                    navController.navigate(TournamentDetailRoute(id))
                                },
                                onLogout = {
                                    navController.navigate(LoginRoute) {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }

                        composable<TournamentDetailRoute> { backStackEntry ->
                            val route: TournamentDetailRoute = backStackEntry.toRoute()
                            TournamentDetailScreen(
                                tournamentId = route.tournamentId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable<ManagePaymentRoute> {
                            ManagePaymentScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
