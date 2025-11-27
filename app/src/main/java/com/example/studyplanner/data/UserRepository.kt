package com.example.studyplanner.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // 1️⃣ Створюємо users/{uid} + стартові tasks
    suspend fun createUserInFirestore(name: String, email: String) {
        val uid = auth.currentUser?.uid ?: throw Exception("User UID is null")

        // --- Користувач ---
        val userData = mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("users")
            .document(uid)
            .set(userData)
            .await()

        // --- Стартові завдання ---
        val tasks = listOf(
            mapOf(
                "id" to "task1",
                "title" to "Welcome Task",
                "description" to "This is your first task",
                "priority" to 1,
                "completed" to false,
                "dueDate" to Timestamp.now()
            ),
            mapOf(
                "id" to "task2",
                "title" to "Example Task",
                "description" to "Edit or delete it later",
                "priority" to 2,
                "completed" to false,
                "dueDate" to Timestamp.now()
            ),
            mapOf(
                "id" to "task3",
                "title" to "Try creating new task",
                "description" to "Use Add Task screen",
                "priority" to 3,
                "completed" to false,
                "dueDate" to Timestamp.now()
            )
        )

        val taskCollection = db.collection("users")
            .document(uid)
            .collection("tasks")

        tasks.forEach { task ->
            val taskId = task["id"] as String
            taskCollection.document(taskId).set(task).await()
        }
    }
}
