package com.example.studyplanner.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import com.example.studyplanner.bloc.TaskState
import com.example.studyplanner.models.TaskItem
import kotlinx.coroutines.launch

@Composable
fun TasksScreen(navController: NavController, taskBloc: TaskBloc) {
    val state by taskBloc.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { taskBloc.onEvent(TaskEvent.LoadTasks) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_task") }) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when (state) {
                is TaskState.Idle -> Text("No tasks", modifier = Modifier.align(Alignment.Center))
                is TaskState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is TaskState.Data -> {
                    val tasks = (state as TaskState.Data).data
                    if (tasks.isEmpty()) {
                        Text("No tasks available", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(tasks) { task ->
                                TaskCard(task) {
                                    // Навігація на TaskDetailScreen по ID
                                    navController.navigate("task_detail/${task.id}")
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                is TaskState.Error -> {
                    val errorMsg = (state as TaskState.Error).message
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: $errorMsg")
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = { scope.launch { taskBloc.onEvent(TaskEvent.LoadTasks) } }) {
                            Text("Retry")
                        }
                    }
                }
                is TaskState.Creating,
                is TaskState.Updating,
                is TaskState.CreateSuccess,
                is TaskState.UpdateSuccess -> {} // ігноруємо в списку
            }
        }
    }
}

@Composable
fun TaskCard(task: TaskItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, fontSize = 14.sp, color = androidx.compose.ui.graphics.Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Priority: ${task.priority}", fontSize = 12.sp)
        }
    }
}
