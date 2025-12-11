package com.example.toodle.data


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update

// basic CRUD functionality
@Dao
interface TaskDao {
    // grab all the tasks from the table
    @Query("SELECT * FROM task ORDER BY createdAt DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = :taskId ")
    suspend fun getTaskById(taskId: Long): Task

    // if there is a conflict we replace the task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task) // returns nothing

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}