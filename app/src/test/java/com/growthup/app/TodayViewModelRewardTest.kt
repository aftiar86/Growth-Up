package com.growthup.app

import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.ui.today.TodayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelRewardTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeTaskDao: FakeReactiveTaskDao
    private lateinit var fakeRewardDao: FakeRewardDao
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var viewModel: TodayViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskDao = FakeReactiveTaskDao()
        fakeRewardDao = FakeRewardDao()
        repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)
        viewModel = TodayViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testZeroTasksDoesNotTriggerCelebration() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(0, state.totalCount)
        assertFalse(state.showCelebrationDialog)
        assertNull(state.celebrationReward)
    }

    @Test
    fun testCompletingAllTasksTriggersCelebration() = runTest {
        viewModel.addTaskForToday("Baca Buku 30 Menit")
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(1, state.totalCount)
        assertEquals(0, state.completedCount)
        assertFalse(state.showCelebrationDialog)

        // Complete the task
        val taskId = state.tasks.first().id
        viewModel.toggleTask(taskId, true)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.completedCount)
        assertEquals(100, state.progressPercent)
        assertTrue(state.showCelebrationDialog)
        assertNotNull(state.celebrationReward)

        // Dismiss dialog
        viewModel.dismissCelebrationDialog()
        assertFalse(viewModel.uiState.value.showCelebrationDialog)

        // Toggling back and forth on the same day should not trigger duplicate celebration
        viewModel.toggleTask(taskId, false)
        advanceUntilIdle()
        viewModel.toggleTask(taskId, true)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showCelebrationDialog)
    }
}
