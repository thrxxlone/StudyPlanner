package com.example.studyplanner.models

import com.google.firebase.Timestamp

data class TaskItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: Int = 0,         // 1 = High, 2 = Medium, 3 = Low, або як тобі потрібно
    val dueDate: Timestamp = Timestamp.now(),
    val completed: Boolean = false
) {

    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "priority" to priority,
        "dueDate" to dueDate,
        "completed" to completed
    )

    companion object {
        fun fromMap(map: Map<String, Any>): TaskItem {
            return TaskItem(
                id = map["id"] as? String ?: "",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                priority = (map["priority"] as? Long)?.toInt() ?: 0,
                dueDate = map["dueDate"] as? Timestamp ?: Timestamp.now(),
                completed = map["completed"] as? Boolean ?: false
            )
        }
    }
}
