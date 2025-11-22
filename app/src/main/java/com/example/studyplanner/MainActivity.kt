package com.example.studyplanner

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studyplanner.ui.theme.StudyPlannerTheme
import com.example.studyplanner.navigation.AppNavGraph
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ініціалізація Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContent {
            StudyPlannerTheme {
                AppNavGraph(
                    onScreenView = { screenName ->
                        logScreenView(screenName)
                    }
                )
            }
        }
    }

    private fun logScreenView(screenName: String) {
        val bundle = android.os.Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
