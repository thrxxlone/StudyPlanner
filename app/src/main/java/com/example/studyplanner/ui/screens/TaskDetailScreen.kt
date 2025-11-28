package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TaskDetailScreen(
    navController: NavController,
    taskId: String,
    taskName: String,
    description: String = "Default task description",
    priority: String = "Medium",
    expiration: String = "01 Nov 2025"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Task Details",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF5123E8), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = taskName,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Description: $description",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Priority: $priority",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Expiration: $expiration",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back", color = Color.White, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Переходимо на EditTaskScreen з параметрами
                        navController.navigate(
                            "edit_task/$taskId/$taskName/$description/$priority/$expiration"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
