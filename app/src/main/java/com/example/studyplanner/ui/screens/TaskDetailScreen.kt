package com.example.studyplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import com.example.studyplanner.models.TaskItem
import kotlinx.coroutines.launch

@Composable
fun TaskDetailScreen(
    navController: NavController,
    taskBloc: TaskBloc,
    taskId: String,
    taskName: String,
    description: String,
    priority: String,
    expiration: String
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Task Details", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Title: $taskName", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Description: $description", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Priority: $priority", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Due: $expiration", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                val route = "edit_task/$taskId/" +
                        "${taskName.replace(" ", "%20")}/" +
                        "${description.replace(" ", "%20")}/" +
                        "$priority/$expiration"
                navController.navigate(route)
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}
