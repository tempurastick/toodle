package com.example.toodle.repository

import androidx.lifecycle.LiveData
import com.example.toodle.data.Task
import com.example.toodle.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {
    // getting all the tasks and storing them
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(taskId: Long) : Task {
       return taskDao.getTaskById(taskId)
    }

}