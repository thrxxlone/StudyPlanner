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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyplanner.bloc.*
import com.example.studyplanner.data.StorageManager
import com.example.studyplanner.data.TaskRepository
import com.example.studyplanner.ui.components.TaskCard
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(navController: NavController) {
    val context = LocalContext.current

    // Створюємо репозиторій та менеджер
    val repository = TaskRepository()
    val storage = StorageManager(context)

    // ViewModel через фабрику
    val taskBloc: TaskBloc = viewModel(
        factory = TaskBlocFactory(repository, storage)
    )

    val state by taskBloc.state.collectAsState()

    LaunchedEffect(Unit) {
        taskBloc.onEvent(TaskEvent.LoadTasks)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    IconButton(onClick = { taskBloc.onEvent(TaskEvent.ForceError) }) {
                        Icon(Icons.Default.Warning, contentDescription = "Force Error")
                    }
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
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(tasks) { t ->
                            TaskCard(
                                title = t.title,
                                description = t.description,
                                priority = t.priority,
                                dueDate = t.dueDate?.let { formatter.format(it.toDate()) } ?: "",
                                onClick = {
                                    val encodedTitle = t.title.replace(" ", "%20")
                                    val encodedDesc = t.description.replace(" ", "%20")
                                    navController.navigate(
                                        "task_detail/$encodedTitle/$encodedDesc/${t.priority}/${t.dueDate?.seconds
                                            ?: 0}"
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
                        Button(onClick = { taskBloc.onEvent(TaskEvent.LoadTasks) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
