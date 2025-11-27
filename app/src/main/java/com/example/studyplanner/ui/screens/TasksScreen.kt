package com.example.studyplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import com.example.studyplanner.bloc.TaskState
import com.example.studyplanner.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TaskBloc = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(TaskEvent.LoadTasks)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    // Кнопка для тестової помилки
                    IconButton(onClick = { viewModel.onEvent(TaskEvent.ForceError) }) {
                        Icon(Icons.Default.Warning, contentDescription = "Force Error")
                    }

                    // Кнопка додавання нового завдання
                    IconButton(onClick = { navController.navigate("add_task") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is TaskState.Idle -> {
                    Text("Idle state...", Modifier.padding(16.dp))
                }

                is TaskState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Loading tasks...")
                    }
                }

                is TaskState.Data -> {
                    val tasks = (state as TaskState.Data).data

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(tasks) { t ->
                            TaskCard(
                                title = t.title,
                                description = t.description,
                                priority = when(t.priority) {
                                    1 -> "High"
                                    2 -> "Medium"
                                    3 -> "Low"
                                    else -> "Unknown"
                                },
                                expiration = t.dueDate.toDate().toString(),
                                onClick = {
                                    val encodedTitle = t.title.replace(" ", "%20")
                                    val encodedDesc = t.description.replace(" ", "%20")
                                    navController.navigate(
                                        "task_detail/$encodedTitle/$encodedDesc/${t.priority}/${t.dueDate.seconds}"
                                    )
                                }
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }

                is TaskState.Error -> {
                    val error = (state as TaskState.Error).message
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(TaskEvent.LoadTasks) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
