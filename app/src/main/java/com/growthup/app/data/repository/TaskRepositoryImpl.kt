package com.growthup.app.data.repository

import com.growthup.app.data.local.TaskDao
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasksForDate(date: String): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDate(date)
    }

    override fun getUncompletedTasksBefore(date: String): Flow<List<TaskEntity>> {
        return taskDao.getUncompletedTasksBefore(date)
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    override suspend fun addTask(title: String, targetDate: String): Long {
        val entity = TaskEntity(
            title = title.trim(),
            targetDate = targetDate,
            isCompleted = false,
            isArchived = false
        )
        return taskDao.insertTask(entity)
    }

    override suspend fun toggleTask(id: Long, isCompleted: Boolean) {
        val existing = taskDao.getTaskById(id) ?: return
        taskDao.updateTask(existing.copy(isCompleted = isCompleted))
    }

    override suspend fun rolloverToTomorrow(task: TaskEntity): Long {
        // 1. Archive the existing task for today so it's marked as evaluated
        taskDao.updateTask(task.copy(isArchived = true))

        // 2. Insert new task targeted for tomorrow
        val tomorrow = LocalDate.now().plusDays(1).toString()
        val rolledOverTask = TaskEntity(
            title = task.title,
            targetDate = tomorrow,
            isCompleted = false,
            isArchived = false
        )
        return taskDao.insertTask(rolledOverTask)
    }

    override suspend fun archiveTask(id: Long) {
        val existing = taskDao.getTaskById(id) ?: return
        taskDao.updateTask(existing.copy(isArchived = true))
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteById(id)
    }
}
