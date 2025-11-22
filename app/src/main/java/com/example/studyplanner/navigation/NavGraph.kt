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
fun AppNavGraph(onScreenView: (String) -> Unit) {
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

            composable("login") {
                onScreenView("LoginScreen")
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }

            composable("home") {
                onScreenView("HomeScreen")
                HomeScreen()
            }

            composable("register") {
                onScreenView("RegisterScreen")
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate("home") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("schedule") {
                onScreenView("ScheduleScreen")
                ScheduleScreen(onBack = { navController.navigate("home") })
            }

            composable("tasks") {
                onScreenView("TasksScreen")
                TasksScreen(onBack = { navController.navigate("home") })
            }

            composable("profile") {
                onScreenView("ProfileScreen")
                ProfileScreen(onBack = { navController.navigate("home") })
            }
        }
    }
}
