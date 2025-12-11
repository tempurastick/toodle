package com.example.toodle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toodle.data.Task
import com.example.toodle.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {

    val allTasks = repository.allTasks

    fun getTaskById(taskId: Long, onResult: (Task) ->Unit) = viewModelScope.launch {
        val task = repository.getTaskById(taskId)
        onResult(task)
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

}