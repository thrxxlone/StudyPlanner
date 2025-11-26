package com.example.studyplanner.bloc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyplanner.models.TaskItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskBloc : ViewModel() {

    private val _state = MutableStateFlow<TaskState>(TaskState.Idle)
    val state: StateFlow<TaskState> = _state

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.LoadTasks -> loadTasks()
            is TaskEvent.ForceError -> forceError()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {

            val previousData: List<TaskItem>? =
                (state.value as? TaskState.Data)?.data

            _state.value = TaskState.Loading(previousData)

            delay(1200) // імітація запиту

            val result = listOf(
                TaskItem("Task 1", "Finish UI for login screen", "High", "01 Nov 2025"),
                TaskItem("Task 2", "Prepare diagrams for project", "Normal", "03 Nov 2025"),
                TaskItem("Task 3", "Learn Navigation Compose", "Low", "10 Nov 2025")
            )

            _state.value = TaskState.Data(result)
        }
    }

    private fun forceError() {
        viewModelScope.launch {
            val previousData: List<TaskItem>? =
                (state.value as? TaskState.Data)?.data

            _state.value = TaskState.Loading(previousData)

            delay(1000)

            _state.value = TaskState.Error(
                message = "Test error occurred",
                oldData = previousData
            )
        }
    }
}
