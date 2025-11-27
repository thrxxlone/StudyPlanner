package com.example.studyplanner.bloc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studyplanner.data.StorageManager
import com.example.studyplanner.data.TaskRepository

class TaskBlocFactory(
    private val repository: TaskRepository,
    private val storage: StorageManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskBloc::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskBloc(repository, storage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
