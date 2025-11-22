package com.example.studyplanner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studyplanner.ui.screens.*

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarScreens = listOf("home", "schedule", "tasks", "profile")

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarScreens) {
                BottomBar(navController = navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {

            // ---------- LOGIN ----------
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }

            // ---------- REGISTER ----------
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ---------- HOME ----------
            composable("home") { HomeScreen() }

            // ---------- OTHER SCREENS ----------
            composable("schedule") { ScheduleScreen(onBack = { navController.navigate("home") }) }
            composable("tasks") { TasksScreen(onBack = { navController.navigate("home") }) }
            composable("profile") { ProfileScreen(onBack = { navController.navigate("home") }) }
        }
    }
}
