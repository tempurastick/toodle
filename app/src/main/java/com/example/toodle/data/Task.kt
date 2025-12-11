package com.example.toodle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
                    val title: String,
                    val description: String,
                    val priority: Long = 1,
                    val isCompleted: Boolean = false,
                    val createdAt: Long = Date().time
)    {
    fun getPriorityColor() : Long {
        // setting the colours here, TODO: swap later
        return when(priority){
            3L -> 0xFFD45959 // red for high
            2L -> 0xFFFFA62B // orange for medium
            else -> 0xFF8CB668 // Green for low
        }
    }

    fun getPriorityText() : String{
        return when (priority) {
            3L -> "High"
            2L -> "Medium"
            else -> "Low"
        }
    }
}