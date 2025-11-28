package com.example.studyplanner.bloc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyplanner.data.StorageManager
import com.example.studyplanner.data.TaskRepository
import com.example.studyplanner.models.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class TaskBloc(
    private val repository: TaskRepository,
    private val storage: StorageManager
) : ViewModel() {

    private val _state = MutableStateFlow<TaskState>(TaskState.Idle)
    val state: StateFlow<TaskState> = _state

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.LoadTasks -> loadTasks()
            is TaskEvent.ForceError -> {
                _state.value = TaskState.Error("Manual error (test)", null)
            }
            is TaskEvent.AddTask -> addTask(event.task)
            is TaskEvent.UpdateTask -> updateTask(event.task)
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _state.value = TaskState.Loading(null)
            try {
                val uid = storage.userUid.firstOrNull()
                    ?: throw Exception("User UID not found")

                val tasks = repository.getTasks(uid)
                _state.value = TaskState.Data(tasks)

            } catch (e: Exception) {
                _state.value = TaskState.Error(
                    message = e.message ?: "Unknown Firestore error",
                    oldData = null
                )
            }
        }
    }

    private fun addTask(task: TaskItem) {
        viewModelScope.launch {
            _state.value = TaskState.Creating
            try {
                val uid = storage.userUid.firstOrNull() ?: throw Exception("User UID not found")
                repository.addTask(uid, task)
                _state.value = TaskState.CreateSuccess
                loadTasks() // оновлення списку після додавання
            } catch (e: Exception) {
                _state.value = TaskState.Error(e.message ?: "Error adding task", null)
            }
        }
    }

    private fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            _state.value = TaskState.Updating
            try {
                val uid = storage.userUid.firstOrNull() ?: throw Exception("User UID not found")
                repository.updateTask(uid, task)
                _state.value = TaskState.UpdateSuccess
                loadTasks() // оновлення списку після редагування
            } catch (e: Exception) {
                _state.value = TaskState.Error(e.message ?: "Error updating task", null)
            }
        }
    }
}
