package com.example.studyplanner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplanner.R
import com.example.studyplanner.bloc.TaskBloc
import com.example.studyplanner.bloc.TaskEvent
import com.example.studyplanner.bloc.TaskState
import com.example.studyplanner.data.StorageManager
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable



@Composable
fun HomeScreen(
    taskBloc: TaskBloc,
    onNavigateToTasks: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onTestCrash: () -> Unit = {}
) {
    val context = LocalContext.current
    val storage = remember { StorageManager(context) }
    var userName by remember { mutableStateOf("User") }

    val state by taskBloc.state.collectAsState()

    LaunchedEffect(Unit) {
        storage.userEmail.collectLatest { email ->
            if (!email.isNullOrEmpty()) userName = email.substringBefore("@")
        }
        taskBloc.onEvent(TaskEvent.LoadTasks)
    }

    val tasks = when (state) {
        is TaskState.Data -> (state as TaskState.Data).data
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(10.dp))

        // ---------- DATE ----------
        Text("Monday", fontSize = 14.sp, color = Color.Gray)
        Text("1 November", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        // CRASH TEST LABEL (як у макеті)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFF4B4B))
                .clickable {
                    onTestCrash()  // ← ТЕПЕР ПРАЦЮЄ
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                "Crash Test",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(14.dp))

        // ---------- TOP RIGHT: SEARCH + PROFILE ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Hello, $userName ✍️",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onNavigateToProfile) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
                }
            }
        }

        // ---------- GREETING ----------
        /*Text(
            "Hello, $userName ✍️",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )*/

        Spacer(Modifier.height(25.dp))

        // ---------- MONTHLY PREVIEW ----------
        Text("Monthly Preview", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCardBlue(
                "7",
                "In Progress",
                modifier = Modifier
                    .width(207.dp)
                    .height(115.dp)
            )

            StatCardGray(
                "22",
                "Completed",
                modifier = Modifier
                    .width(172.dp)
                    .height(114.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCardGray(
                "2",
                "Priority Tasks",
                modifier = Modifier
                    .width(207.dp)
                    .height(115.dp)
            )

            StatCardGray(
                "7",
                "Overdue",
                modifier = Modifier
                    .width(172.dp)
                    .height(114.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- MY TASKS ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Task", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onNavigateToTasks) {
                Text("See all",  fontSize = 14.sp)
            }
        }

        // FILTER BUTTON ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TaskFilter("All", true)
            TaskFilter("Recently")
            TaskFilter("Today")
            TaskFilter("Deadline")
        }

        Spacer(Modifier.height(14.dp))

        // ---------- SAMPLE TASK LIST ----------
        TaskItemCard("Task #1", "8 November, 2025")
        Spacer(Modifier.height(10.dp))
        TaskItemCard("Task #1", "8 November, 2025")

        Spacer(Modifier.height(30.dp))

        // ---------- NEWS ----------
        Text("What's new?", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            NewsCard(R.drawable.sample_news_1, "ІІ етап 83-ї ... інституту")
            Spacer(Modifier.width(12.dp))
            NewsCard(R.drawable.sample_news_2, "Нові горизонти ... інституту")
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun StatCardBlue(number: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(135.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4169E1), Color(0xFF0000C8))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(number, fontSize = 26.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
fun StatCardGray(number: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(135.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEDEDED)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(number, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun TaskFilter(text: String, selected: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color.Black else Color(0xFFEAEAEA))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TaskItemCard(title: String, date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F1F1))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(date, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
fun NewsCard(imageRes: Int, title: String) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF3F3F3))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "News",
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )
        Text(
            title,
            modifier = Modifier.padding(12.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
