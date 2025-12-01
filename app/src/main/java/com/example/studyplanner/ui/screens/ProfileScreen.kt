package com.example.studyplanner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplanner.R
import com.example.studyplanner.data.StorageManager
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


@Composable
fun ProfileScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val storage = remember { StorageManager(context) }

    var userEmail by remember { mutableStateOf("Loading...") }
    var userName by remember { mutableStateOf("User") }

    val coroutine = rememberCoroutineScope()

    // 🔹 Завантажити email з DataStore
    LaunchedEffect(true) {
        storage.userEmail.collectLatest { email ->
            if (email != null) {
                userEmail = email
                userName = email.substringBefore("@")  // автоматично генеруємо ім’я
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // Аватар
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Ім’я та email з Firebase / DataStore
        Text(
            text = userName,
            fontSize = 28.sp,
            color = Color.Black
        )

        Text(
            text = userEmail,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Кнопка виходу
        Button(
            onClick = {
                coroutine.launch {
                    storage.clear()  // 🔹 Очищаємо DataStore
                }
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Log Out", fontSize = 16.sp)
        }
    }
}
