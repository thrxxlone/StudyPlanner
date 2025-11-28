package com.example.studyplanner.bloc

import com.example.studyplanner.models.TaskItem

sealed class TaskEvent {
    object LoadTasks : TaskEvent()
    object ForceError : TaskEvent()

    // Нові події для створення та редагування
    data class AddTask(val task: TaskItem) : TaskEvent()
    data class UpdateTask(val task: TaskItem) : TaskEvent()

    // Видалення завдання
    data class DeleteTask(val taskId: String) : TaskEvent()
}
