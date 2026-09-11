package com.example.societyconnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.societyconnect.data.repository.AuthRepository
import com.example.societyconnect.data.repository.ProfileRepository
import com.example.societyconnect.presentation.admin.AdminPaymentScreen
import com.example.societyconnect.presentation.admin.AdminAnnouncementScreen
import com.example.societyconnect.presentation.admin.AdminComplaintScreen
import com.example.societyconnect.presentation.admin.AdminHomeScreen
import com.example.societyconnect.presentation.announcements.AnnouncementScreen
import com.example.societyconnect.presentation.auth.LoginScreen
import com.example.societyconnect.presentation.auth.RegisterScreen
import com.example.societyconnect.presentation.complaints.ComplaintScreen
import com.example.societyconnect.presentation.events.EventScreen
import com.example.societyconnect.presentation.home.FeatureScreen
import com.example.societyconnect.presentation.home.HomeScreen
import com.example.societyconnect.presentation.profile.ProfileScreen
import com.example.societyconnect.presentation.admin.AdminEventScreen
import com.example.societyconnect.presentation.payments.PaymentScreen
import com.example.societyconnect.presentation.admin.AdminVisitorScreen



object Routes {

    // --------------------------------------------------
    // AUTH
    // --------------------------------------------------

    const val LOGIN = "login"
    const val REGISTER = "register"

    const val ADMIN_EVENTS = "admin_events"

    // --------------------------------------------------
    // RESIDENT
    // --------------------------------------------------

    const val HOME = "home"
    const val ANNOUNCEMENTS = "announcements"
    const val EVENTS = "events"
    const val COMPLAINTS = "complaints"
    const val PAYMENTS = "payments"
    const val VISITORS = "visitors"
    const val PROFILE = "profile"
    const val ADMIN_PAYMENTS = "admin_payments"

    // --------------------------------------------------
    // ADMIN
    // --------------------------------------------------

    const val ADMIN_HOME = "admin_home"
    const val ADMIN_COMPLAINTS = "admin_complaints"
    const val ADMIN_ANNOUNCEMENTS = "admin_announcements"

    const val ADMIN_VISITORS = "admin_visitors"
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val authRepository = remember {
        AuthRepository()
    }

    val profileRepository = remember {
        ProfileRepository()
    }

    /*
     * Start destination is initially unknown.
     * We first check whether the user is logged in
     * and then check their role.
     */
    var startDestination by remember {
        mutableStateOf<String?>(null)
    }


    // --------------------------------------------------
    // CHECK LOGIN + ROLE
    // --------------------------------------------------

    LaunchedEffect(Unit) {

        if (authRepository.currentUser == null) {

            // User is not logged in
            startDestination = Routes.LOGIN

        } else {

            // User is logged in.
            // Check whether the user is ADMIN or RESIDENT.
            profileRepository.getCurrentUserRole { result ->

                val role = result.getOrNull()

                startDestination =
                    if (role == "ADMIN") {
                        Routes.ADMIN_HOME
                    } else {
                        Routes.HOME
                    }
            }
        }
    }


    /*
     * Wait until we know the user's role.
     */
    if (startDestination == null) {
        return
    }


    // --------------------------------------------------
    // NAVIGATION HOST
    // --------------------------------------------------

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {


        // ==================================================
        // LOGIN
        // ==================================================

        composable(Routes.LOGIN) {

            LoginScreen(

                onLoginSuccess = {

                    /*
                     * After successful login,
                     * check the user's Firestore role.
                     */
                    profileRepository.getCurrentUserRole { result ->

                        val role = result.getOrNull()

                        val destination =
                            if (role == "ADMIN") {
                                Routes.ADMIN_HOME
                            } else {
                                Routes.HOME
                            }

                        navController.navigate(destination) {

                            /*
                             * Remove login from back stack.
                             * User cannot press Back and return
                             * to the login screen.
                             */
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                },

                onRegisterClick = {

                    navController.navigate(
                        Routes.REGISTER
                    )
                }
            )
        }


        // ==================================================
        // REGISTER
        // ==================================================

        composable(Routes.REGISTER) {

            RegisterScreen(

                onRegisterSuccess = {

                    /*
                     * New registrations are RESIDENT by default.
                     */
                    navController.navigate(
                        Routes.HOME
                    ) {

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


        // ==================================================
        // RESIDENT HOME
        // ==================================================

        composable(Routes.HOME) {

            HomeScreen(

                onLogout = {

                    authRepository.logout()

                    navController.navigate(
                        Routes.LOGIN
                    ) {

                        popUpTo(0)
                    }
                },

                onFeatureClick = { route ->

                    navController.navigate(route)
                }
            )
        }


        // ==================================================
        // PROFILE
        // ==================================================

        composable(Routes.PROFILE) {

            ProfileScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // RESIDENT ANNOUNCEMENTS
        // ==================================================

        composable(Routes.ANNOUNCEMENTS) {

            AnnouncementScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // RESIDENT EVENTS
        // ==================================================

        composable(Routes.EVENTS) {

            EventScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // RESIDENT COMPLAINTS
        // ==================================================

        composable(Routes.COMPLAINTS) {

            ComplaintScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // PAYMENTS
        // ==================================================

        composable(Routes.PAYMENTS) {

            PaymentScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // VISITORS
        // ==================================================

        composable(Routes.VISITORS) {

            FeatureScreen(
                title = "Visitors",

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // ADMIN HOME
        // ==================================================

        composable(Routes.ADMIN_HOME) {

            AdminHomeScreen(

                // ------------------------------------------
                // MANAGE COMPLAINTS
                // ------------------------------------------

                onComplaintsClick = {

                    navController.navigate(
                        Routes.ADMIN_COMPLAINTS
                    )
                },


                // ------------------------------------------
                // MANAGE ANNOUNCEMENTS
                // ------------------------------------------

                onAnnouncementsClick = {

                    /*
                     * IMPORTANT:
                     * Admin must go to the ADMIN announcement
                     * screen, NOT the resident announcement screen.
                     */
                    navController.navigate(
                        Routes.ADMIN_ANNOUNCEMENTS
                    )
                },

                onPaymentsClick = {

                    navController.navigate(
                        Routes.ADMIN_PAYMENTS
                    )
                },

                onVisitorsClick = {

                    navController.navigate(
                        Routes.ADMIN_VISITORS
                    )
                },



                // ------------------------------------------
                // MANAGE EVENTS
                // ------------------------------------------

                onEventsClick = {

                    navController.navigate(
                        Routes.ADMIN_EVENTS
                    )
                },


                // ------------------------------------------
                // LOGOUT
                // ------------------------------------------

                onLogout = {

                    authRepository.logout()

                    navController.navigate(
                        Routes.LOGIN
                    ) {

                        popUpTo(0)
                    }
                }
            )
        }


        // ==================================================
        // ADMIN COMPLAINTS
        // ==================================================

        composable(Routes.ADMIN_COMPLAINTS) {

            AdminComplaintScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // ==================================================
        // ADMIN ANNOUNCEMENTS
        // ==================================================

        composable(Routes.ADMIN_ANNOUNCEMENTS) {

            AdminAnnouncementScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ADMIN_EVENTS) {

            AdminEventScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.ADMIN_PAYMENTS) {

            AdminPaymentScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.ADMIN_VISITORS) {

            AdminVisitorScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}