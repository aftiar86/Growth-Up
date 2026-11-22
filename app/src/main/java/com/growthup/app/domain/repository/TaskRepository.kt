package com.growthup.app.domain.repository

import com.growthup.app.data.model.TaskEntity
import kotlinx.coroutines.flow.Flow

data class HistorySummary(
    val totalTasks: Int,
    val completedTasks: Int,
    val completionPercentage: Int
)

interface TaskRepository {
    fun getTasksForDate(date: String): Flow<List<TaskEntity>>
    fun getUncompletedTasksBefore(date: String): Flow<List<TaskEntity>>
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun addTask(title: String, targetDate: String): Long
    suspend fun toggleTask(id: Long, isCompleted: Boolean)
    suspend fun rolloverToTomorrow(task: TaskEntity): Long
    suspend fun archiveTask(id: Long)
    suspend fun deleteTask(id: Long)
}
