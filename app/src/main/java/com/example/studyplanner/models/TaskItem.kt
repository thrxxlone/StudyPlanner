package com.example.studyplanner.models

data class TaskItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val priority: String = "",
    val expiration: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "priority" to priority,
        "expiration" to expiration
    )

    companion object {
        fun fromMap(map: Map<String, Any>): TaskItem {
            return TaskItem(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                priority = map["priority"] as? String ?: "",
                expiration = map["expiration"] as? String ?: ""
            )
        }
    }
}
