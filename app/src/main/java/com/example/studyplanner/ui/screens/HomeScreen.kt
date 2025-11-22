package com.example.studyplanner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplanner.R

@Composable
fun HomeScreen(
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToTasks: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ---------- DATE ----------
        Text("Monday", fontSize = 14.sp, color = Color.Gray)
        Text("1 November", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        // ---------- TOP: SEARCH + PROFILE ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            Spacer(Modifier.width(12.dp))

            Box {
                IconButton(onClick = { onNavigateToProfile() }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(28.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF4B4B))
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // -------- GREETING --------
        Text(
            "Привіт, Oleksandr✍️",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // ---------- UPCOMING DEADLINE ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF4169E1), Color(0xFF00008B))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Upcoming Deadline task",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- MONTHLY PREVIEW ----------
        Text(
            "Monthly Preview",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("22", "Done")
            StatCard("7", "In progress")
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("12", "Ongoing")
            StatCard("14", "Waiting for review")
        }

        Spacer(Modifier.height(28.dp))

        // ---------- NEWS ----------
        Text(
            "Що нового?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            NewsCard(R.drawable.sample_news_1, "ІІ етап 83-ї ... інституту")
            Spacer(Modifier.width(10.dp))
            NewsCard(R.drawable.sample_news_2, "Нові горизонти ... інституту")
        }
    }
}

@Composable
fun StatCard(number: String, label: String) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF4169E1), Color(0xFF00008B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(number, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 14.sp, color = Color.White)
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
