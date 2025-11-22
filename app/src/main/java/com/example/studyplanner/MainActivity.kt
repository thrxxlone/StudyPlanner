package com.example.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studyplanner.navigation.AppNavGraph
import com.example.studyplanner.ui.theme.StudyPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyPlannerTheme {
                AppNavGraph()
            }
        }
    }
}
