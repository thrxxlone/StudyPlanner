package com.example.studyplanner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.studyplanner.MainActivity
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.data.StorageManager
import com.example.studyplanner.data.TaskRepository
import com.example.studyplanner.models.TaskItem
import com.example.studyplanner.ui.screens.*

@Composable
fun AppNavGraph(onScreenView: (String) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarScreens = listOf("home", "schedule", "tasks", "profile")

    val context = LocalContext.current

    // Єдиний екземпляр TaskBloc для всіх екранів
    val storage = remember { StorageManager(context) }
    val repository = remember { TaskRepository() }
    val taskBloc = remember { TaskBloc(repository, storage) }

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
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }

            composable("home") {
                onScreenView("HomeScreen")
                HomeScreen(
                    onTestCrash = { (context as? MainActivity)?.generateTestCrash() }
                )
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
                TasksScreen(navController = navController, taskBloc = taskBloc)
            }

            composable("profile") {
                onScreenView("ProfileScreen")
                ProfileScreen(onBack = { navController.navigate("home") })
            }

            composable("add_task") {
                onScreenView("AddTaskScreen")
                AddTaskScreen(navController = navController, taskBloc = taskBloc)
            }

            // TaskDetailScreen
            composable(
                "task_detail/{taskId}/{name}/{description}/{priority}/{expiration}",
                arguments = listOf(
                    navArgument("taskId") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("description") { type = NavType.StringType },
                    navArgument("priority") { type = NavType.StringType },
                    navArgument("expiration") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                val name = backStackEntry.arguments?.getString("name")?.replace("%20", " ") ?: ""
                val description = backStackEntry.arguments?.getString("description")?.replace("%20", " ") ?: ""
                val priority = backStackEntry.arguments?.getString("priority") ?: ""
                val expiration = backStackEntry.arguments?.getString("expiration") ?: ""

                TaskDetailScreen(
                    navController = navController,
                    taskId = taskId,
                    taskName = name,
                    description = description,
                    priority = priority,
                    expiration = expiration
                )
            }

            // EditTaskScreen
            composable(
                "edit_task/{taskId}/{title}/{description}/{priority}/{expiration}",
                arguments = listOf(
                    navArgument("taskId") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("description") { type = NavType.StringType },
                    navArgument("priority") { type = NavType.StringType },
                    navArgument("expiration") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                val title = backStackEntry.arguments?.getString("title")?.replace("%20", " ") ?: ""
                val description = backStackEntry.arguments?.getString("description")?.replace("%20", " ") ?: ""
                val priority = backStackEntry.arguments?.getString("priority") ?: ""
                val expiration = backStackEntry.arguments?.getString("expiration") ?: ""

                val task = TaskItem(
                    id = taskId,
                    title = title,
                    description = description,
                    priority = when (priority) {
                        "Low" -> 1
                        "Normal" -> 2
                        "High" -> 3
                        else -> 2
                    },
                    completed = false
                )

                EditTaskScreen(
                    navController = navController,
                    taskBloc = taskBloc,
                    task = task
                )
            }
        }
    }
}
