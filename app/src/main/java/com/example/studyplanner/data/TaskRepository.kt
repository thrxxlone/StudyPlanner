package com.example.studyplanner.data

import com.example.studyplanner.models.TaskItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log


class TaskRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun userTasksRef(uid: String) =
        db.collection("users").document(uid).collection("tasks")

    /** Отримати всі задачі */
    suspend fun getTasks(uid: String): List<TaskItem> {
        val snapshot = userTasksRef(uid).get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.data?.let { TaskItem.fromMap(it) }
        }
    }

    /** Додати задачу */
    suspend fun addTask(uid: String, task: TaskItem) {

        // Якщо id порожній → генеруємо task1, task2...
        val fixedTask = if (task.id.isBlank()) {
            task.copy(id = "task_${System.currentTimeMillis()}")
        } else task

        userTasksRef(uid)
            .document(fixedTask.id)
            .set(fixedTask.toMap())
            .await()

        Log.d("TaskRepository", "Task added to Firestore: $fixedTask")
    }

    /** Оновити задачу */
    suspend fun updateTask(uid: String, task: TaskItem) {
        userTasksRef(uid)
            .document(task.id)
            .update(task.toMap())
            .await()
    }

    /** Видалити задачу */
    suspend fun deleteTask(uid: String, id: String) {
        userTasksRef(uid)
            .document(id)
            .delete()
            .await()
    }
}
