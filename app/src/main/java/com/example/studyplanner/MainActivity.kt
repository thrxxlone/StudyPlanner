package com.example.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studyplanner.ui.theme.StudyPlannerTheme
import com.example.studyplanner.navigation.AppNavGraph
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ініціалізація Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Ініціалізація Firebase Crashlytics
        crashlytics = FirebaseCrashlytics.getInstance()

        setContent {
            StudyPlannerTheme {
                AppNavGraph(
                    onScreenView = { screenName ->
                        logScreenView(screenName)
                        crashlytics.setCustomKey("current_screen", screenName)
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

    // Метод для тестової помилки Crashlytics
    fun generateTestCrash() {
        crashlytics.log("Test crash triggered")
        throw RuntimeException("Test Crash for Firebase Crashlytics")
    }
}
