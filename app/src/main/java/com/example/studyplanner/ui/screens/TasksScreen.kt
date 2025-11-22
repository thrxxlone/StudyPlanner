package com.example.studyplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TasksScreen(onBack: () -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("Tasks Screen")
    }
}

