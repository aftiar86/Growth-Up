package com.growthup.app

import com.growthup.app.data.local.TaskDao
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.domain.repository.TaskRepository
import com.growthup.app.ui.today.TodayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FakeReactiveTaskDao : TaskDao {
    private val _tasksFlow = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks = mutableListOf<TaskEntity>()
    private var idCounter = 1L

    private fun emit() {
        _tasksFlow.value = tasks.toList()
    }

    override fun getTasksForDate(date: String): Flow<List<TaskEntity>> {
        return _tasksFlow.map { list -> list.filter { it.targetDate == date && !it.isArchived } }
    }

    override suspend fun getTaskById(id: Long): TaskEntity? {
        return tasks.find { it.id == id }
    }

    override fun getUncompletedTasksBefore(date: String): Flow<List<TaskEntity>> {
        return _tasksFlow.map { list -> list.filter { it.targetDate < date && !it.isCompleted && !it.isArchived } }
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return _tasksFlow
    }

    override suspend fun insertTask(task: TaskEntity): Long {
        val assignedId = if (task.id == 0L) idCounter++ else task.id
        val entity = task.copy(id = assignedId)
        tasks.removeAll { it.id == assignedId }
        tasks.add(entity)
        emit()
        return assignedId
    }

    override suspend fun updateTask(task: TaskEntity) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            emit()
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasks.removeAll { it.id == task.id }
        emit()
    }

    override suspend fun deleteById(id: Long) {
        tasks.removeAll { it.id == id }
        emit()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeDao: FakeReactiveTaskDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TodayViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeReactiveTaskDao()
        repository = TaskRepositoryImpl(fakeDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testProgressCalculation() = runTest(testDispatcher) {
        val today = LocalDate.now().toString()
        val id1 = repository.addTask("Tugas 1", today)
        val id2 = repository.addTask("Tugas 2", today)

        viewModel = TodayViewModel(repository)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(2, state.totalCount)
        assertEquals(0, state.completedCount)
        assertEquals(0, state.progressPercent)

        viewModel.toggleTask(id1, true)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.completedCount)
        assertEquals(50, state.progressPercent)
    }
}
