package com.growthup.app

import com.growthup.app.domain.repository.TaskRepository
import com.growthup.app.ui.tomorrow.TomorrowViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class TomorrowViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeDao: FakeReactiveTaskDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TomorrowViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeReactiveTaskDao()
        repository = com.growthup.app.data.repository.TaskRepositoryImpl(fakeDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testAddTomorrowTaskAndRollover() = runTest(testDispatcher) {
        val today = LocalDate.now().toString()
        val tomorrow = LocalDate.now().plusDays(1).toString()

        // 1. Add uncompleted task for today
        val todayTaskId = repository.addTask("Tugas Tertunda", today)

        viewModel = TomorrowViewModel(repository)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(1, state.uncompletedPreviousTasks.size)
        assertEquals(0, state.tomorrowTasks.size)

        // 2. Add new plan for tomorrow
        viewModel.addTaskForTomorrow("Rencana Baru Besok")
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.tomorrowTasks.size)
        assertEquals("Rencana Baru Besok", state.tomorrowTasks[0].title)

        // 3. Rollover the previous uncompleted task
        val uncompletedTask = state.uncompletedPreviousTasks[0]
        viewModel.rolloverTask(uncompletedTask)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(0, state.uncompletedPreviousTasks.size)
        assertEquals(2, state.tomorrowTasks.size)
    }
}
