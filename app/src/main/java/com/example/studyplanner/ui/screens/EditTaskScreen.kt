package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import com.example.studyplanner.models.TaskItem
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    navController: NavController,
    taskBloc: TaskBloc,
    task: TaskItem // передаємо завдання, що редагується
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    var priority by remember { mutableStateOf(
        when(task.priority){
            1 -> "Low"
            2 -> "Normal"
            3 -> "High"
            else -> "Normal"
        }
    ) }

    var status by remember { mutableStateOf(if(task.completed) "Completed" else "Active") }

    var showPrioritySheet by remember { mutableStateOf(false) }
    var showStatusSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val storage = LocalContext.current.let { com.example.studyplanner.data.StorageManager(it) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
        ) {
            Text("Edit Task", fontSize = 28.sp, color = Color.Black)
            Spacer(Modifier.height(20.dp))

            TaskInputField("Title", title) { title = it }
            Spacer(Modifier.height(12.dp))
            TaskInputField("Description", description) { description = it }
            Spacer(Modifier.height(12.dp))

            SelectorButton(
                title = "Priority",
                value = priority,
                onClick = { showPrioritySheet = true }
            )
            Spacer(Modifier.height(12.dp))

            SelectorButton(
                title = "Status",
                value = status,
                onClick = { showStatusSheet = true }
            )
            Spacer(Modifier.height(25.dp))

            Button(
                onClick = {
                    scope.launch {
                        val uid = storage.userUid.firstOrNull()
                        if(uid != null){
                            val updatedTask = task.copy(
                                title = title,
                                description = description,
                                priority = when(priority){
                                    "Low" -> 1
                                    "Normal" -> 2
                                    "High" -> 3
                                    else -> 2
                                },
                                completed = status != "Active",
                                dueDate = task.dueDate // можна додати редагування дати при бажанні
                            )
                            taskBloc.onEvent(TaskEvent.UpdateTask(updatedTask))
                            navController.popBackStack()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5123E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes", color = Color.White, fontSize = 18.sp)
            }
        }

        if(showPrioritySheet){
            SelectionSheet(
                title = "Select priority",
                options = listOf("Low", "Normal", "High"),
                onSelect = { priority = it; showPrioritySheet = false },
                onDismiss = { showPrioritySheet = false }
            )
        }

        if(showStatusSheet){
            SelectionSheet(
                title = "Select status",
                options = listOf("Active", "Completed", "Overdue"),
                onSelect = { status = it; showStatusSheet = false },
                onDismiss = { showStatusSheet = false }
            )
        }
    }
}
