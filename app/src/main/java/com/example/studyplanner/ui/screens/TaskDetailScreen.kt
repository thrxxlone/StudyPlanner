package com.example.studyplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import kotlinx.coroutines.launch

@Composable
fun TaskDetailScreen(
    navController: NavController,
    taskBloc: TaskBloc,
    taskId: String
) {
    val scope = rememberCoroutineScope()

    // Отримуємо завдання через Bloc
    val task = (taskBloc.state.value as? com.example.studyplanner.bloc.TaskState.Data)
        ?.data
        ?.find { it.id == taskId }

    if (task == null) {
        Text("Task not found", fontSize = 20.sp)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Task Details", fontSize = 28.sp)
        Spacer(Modifier.height(16.dp))

        Text("Title: ${task.title}", fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))

        Text("Description: ${task.description}", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        Text("Priority: ${task.priority}", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        Text("Due: ${task.dueDate?.toDate()?.toString() ?: "None"}", fontSize = 16.sp)

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            // 🔥 Тепер Edit працює через taskId
            Button(onClick = {
                navController.navigate("edit_task/$taskId")
            }) {
                Text("Edit")
            }

            Button(
                onClick = {
                    scope.launch {
                        taskBloc.onEvent(TaskEvent.DeleteTask(taskId))
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}
