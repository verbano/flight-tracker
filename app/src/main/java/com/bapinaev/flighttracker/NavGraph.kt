package com.bapinaev.flighttracker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bapinaev.flighttracker.screen.LoginScreen
import com.bapinaev.flighttracker.screen.ProfileScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {

            LoginScreen(
                onLoginClick = { _, _ -> navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Profile.route) {

            ProfileScreen()
        }
    }
}
