package com.example.toodle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toodle.repository.TaskRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return TaskViewModel(repository) as T
        }

        throw IllegalArgumentException("not the correct viewModel class...")

    }
}