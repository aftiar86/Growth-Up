package com.growthup.app

import com.growthup.app.data.local.TaskDao
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FakeTaskDao : TaskDao {
    val tasks = mutableListOf<TaskEntity>()
    private var idCounter = 1L

    override fun getTasksForDate(date: String): Flow<List<TaskEntity>> {
        return flowOf(tasks.filter { it.targetDate == date && !it.isArchived })
    }

    override suspend fun getTaskById(id: Long): TaskEntity? {
        return tasks.find { it.id == id }
    }

    override fun getUncompletedTasksBefore(date: String): Flow<List<TaskEntity>> {
        return flowOf(tasks.filter { it.targetDate < date && !it.isCompleted && !it.isArchived })
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return flowOf(tasks.toList())
    }

    override suspend fun insertTask(task: TaskEntity): Long {
        val assignedId = if (task.id == 0L) idCounter++ else task.id
        val entity = task.copy(id = assignedId)
        tasks.removeAll { it.id == assignedId }
        tasks.add(entity)
        return assignedId
    }

    override suspend fun updateTask(task: TaskEntity) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasks.removeAll { it.id == task.id }
    }

    override suspend fun deleteById(id: Long) {
        tasks.removeAll { it.id == id }
    }
}

class TaskRepositoryTest {
    private lateinit var fakeDao: FakeTaskDao
    private lateinit var repository: TaskRepository

    @Before
    fun setUp() {
        fakeDao = FakeTaskDao()
        repository = TaskRepositoryImpl(fakeDao)
    }

    @Test
    fun testAddAndToggleTask() = runBlocking {
        val today = LocalDate.now().toString()
        val id = repository.addTask("Minum air 2 liter", today)
        assertTrue(id > 0)

        val tasks = repository.getTasksForDate(today).first()
        assertEquals(1, tasks.size)
        assertEquals("Minum air 2 liter", tasks[0].title)

        repository.toggleTask(id, true)
        val updatedTasks = repository.getTasksForDate(today).first()
        assertTrue(updatedTasks[0].isCompleted)
    }

    @Test
    fun testRolloverToTomorrow() = runBlocking {
        val today = LocalDate.now().toString()
        val tomorrow = LocalDate.now().plusDays(1).toString()

        val id = repository.addTask("Belajar Kotlin Coroutines", today)
        val task = fakeDao.getTaskById(id)!!

        val newId = repository.rolloverToTomorrow(task)
        assertTrue(newId > 0)

        // Today's original task should be archived so it doesn't show in today's active list
        val todayTasks = repository.getTasksForDate(today).first()
        assertEquals(0, todayTasks.size)

        // Tomorrow should now contain the task
        val tomorrowTasks = repository.getTasksForDate(tomorrow).first()
        assertEquals(1, tomorrowTasks.size)
        assertEquals("Belajar Kotlin Coroutines", tomorrowTasks[0].title)
    }

    @Test
    fun testArchiveTask() = runBlocking {
        val today = LocalDate.now().toString()
        val id = repository.addTask("Tugas yang dibatalkan", today)
        repository.archiveTask(id)

        val todayTasks = repository.getTasksForDate(today).first()
        assertEquals(0, todayTasks.size)
    }
}
