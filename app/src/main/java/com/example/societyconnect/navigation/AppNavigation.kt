package com.example.societyconnect.navigation
import com.example.societyconnect.presentation.profile.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.societyconnect.data.repository.AuthRepository
import com.example.societyconnect.presentation.auth.LoginScreen
import com.example.societyconnect.presentation.auth.RegisterScreen
import com.example.societyconnect.presentation.home.HomeScreen
import com.example.societyconnect.presentation.home.FeatureScreen
import com.example.societyconnect.presentation.announcements.AnnouncementScreen
import com.example.societyconnect.presentation.events.EventScreen
import com.example.societyconnect.presentation.complaints.ComplaintScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ANNOUNCEMENTS = "announcements"
    const val EVENTS = "events"
    const val COMPLAINTS = "complaints"
    const val PAYMENTS = "payments"
    const val VISITORS = "visitors"
    const val PROFILE = "profile"
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val authRepository = AuthRepository()

    val startDestination =
        if (authRepository.currentUser != null) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // REGISTER
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // HOME
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    authRepository.logout()

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }
                },
                onFeatureClick = { route ->
                    navController.navigate(route)
                }
            )
        }

        // ANNOUNCEMENTS
        composable(Routes.ANNOUNCEMENTS) {
            AnnouncementScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // EVENTS
        composable(Routes.EVENTS) {
            EventScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // COMPLAINTS
        composable(Routes.COMPLAINTS) {
            ComplaintScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // PAYMENTS
        composable(Routes.PAYMENTS) {
            FeatureScreen(
                title = "Payments",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // VISITORS
        composable(Routes.VISITORS) {
            FeatureScreen(
                title = "Visitors",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // PROFILE
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}