package com.example.studyplanner.bloc

sealed class TaskEvent {
    object LoadTasks : TaskEvent()
    object ForceError : TaskEvent()
}
