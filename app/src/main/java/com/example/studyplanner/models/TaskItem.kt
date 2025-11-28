package com.example.studyplanner.models

import com.google.firebase.Timestamp

data class TaskItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: Int = 0,
    val dueDate: Timestamp? = null,
    val completed: Boolean = false
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "priority" to priority,
        "dueDate" to (dueDate ?: Timestamp.now()),
        "completed" to completed
    )

    companion object {
        fun fromMap(map: Map<String, Any>): TaskItem {
            val priorityValue = when(val p = map["priority"]) {
                is Long -> p.toInt()
                is Int -> p
                else -> 0
            }
            return TaskItem(
                id = map["id"] as? String ?: "",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                priority = priorityValue,
                dueDate = map["dueDate"] as? Timestamp,
                completed = map["completed"] as? Boolean ?: false
            )
        }
    }
}
