package com.example.studyplanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", Icons.Default.Home)
    object Schedule : BottomNavItem("schedule", Icons.Default.CalendarMonth)
    object Tasks : BottomNavItem("tasks", Icons.Default.ListAlt)
    object Profile : BottomNavItem("profile", Icons.Default.AccountCircle)
}
