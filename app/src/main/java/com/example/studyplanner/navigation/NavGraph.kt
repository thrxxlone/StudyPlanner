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

    // Єдиний екземпляр TaskBloc
    val storage = remember { StorageManager(context) }
    val repository = remember { TaskRepository() }
    val taskBloc = remember { TaskBloc(repository, storage) }

    Scaffold(
        bottomBar = { if (currentRoute in bottomBarScreens) BottomBar(navController) }
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

            composable("home") { HomeScreen(onTestCrash = { (context as? MainActivity)?.generateTestCrash() }) }

            composable("register") { RegisterScreen(
                onRegisterSuccess = { navController.navigate("home") },
                onBack = { navController.popBackStack() }
            )}

            composable("schedule") { ScheduleScreen(onBack = { navController.navigate("home") }) }

            composable("tasks") { TasksScreen(navController, taskBloc) }

            composable("profile") { ProfileScreen(onBack = { navController.navigate("home") }) }

            composable("add_task") { AddTaskScreen(navController, taskBloc) }

            // TaskDetailScreen по taskId
            composable(
                "task_detail/{taskId}",
                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                val task = (taskBloc.state.value as? com.example.studyplanner.bloc.TaskState.Data)
                    ?.data?.find { it.id == taskId }

                if (task != null) {
                    TaskDetailScreen(
                        navController = navController,
                        taskBloc = taskBloc,
                        taskId = task.id,
                        taskName = task.title,
                        description = task.description,
                        priority = task.priority.toString(),
                        expiration = task.dueDate?.toDate()?.toString() ?: ""
                    )
                }
            }

            // EditTaskScreen по taskId
            composable(
                "edit_task/{taskId}",
                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                val task = (taskBloc.state.value as? com.example.studyplanner.bloc.TaskState.Data)
                    ?.data?.find { it.id == taskId }

                if (task != null) {
                    EditTaskScreen(navController = navController, taskBloc = taskBloc, task = task)
                }
            }
        }
    }
}
