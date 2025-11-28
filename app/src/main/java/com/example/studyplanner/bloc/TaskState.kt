package com.example.studyplanner.bloc

import com.example.studyplanner.models.TaskItem

sealed class TaskState {
    object Idle : TaskState()
    data class Loading(val oldData: List<TaskItem>?) : TaskState()
    data class Data(val data: List<TaskItem>) : TaskState()
    data class Error(val message: String, val oldData: List<TaskItem>?) : TaskState()

    // Нові стани для створення та редагування
    object Creating : TaskState()
    object Updating : TaskState()
    object CreateSuccess : TaskState()
    object UpdateSuccess : TaskState()
}
