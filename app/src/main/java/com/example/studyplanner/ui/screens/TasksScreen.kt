package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Модель завдання для hardcoded даних
data class Task(
    val name: String,
    val description: String,
    val priority: String,
    val expiration: String
)

@Composable
fun TasksScreen(navController: NavController) {

    // Hardcoded список завдань
    val tasks = listOf(
        Task("Task 1", "Finish UI for login screen", "High", "01 Nov 2025"),
        Task("Task 2", "Prepare project documentation", "Medium", "03 Nov 2025"),
        Task("Task 3", "Test Firebase integration", "Low", "05 Nov 2025")
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            // Верхній бар
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("My Tasks", fontSize = 28.sp, color = Color.Black)
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, contentDescription = "menu")
                }
            }

            // Список завдань
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(tasks) { task ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(vertical = 10.dp)
                            .background(
                                color = Color(0xFF5123E8),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                // Перехід на деталі завдання
                                navController.navigate("task_detail/${task}")
                            }
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(task.name, fontSize = 20.sp, color = Color.White)
                            Text(task.description, fontSize = 14.sp, color = Color.White)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Priority: ${task.priority}", color = Color.White, fontSize = 12.sp)
                                Text("Expires: ${task.expiration}", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Плаваюча зелена кнопка "+"
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF32CD32), CircleShape)
                    .clickable {
                        navController.navigate("add_task")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
