package com.growthup.app

import com.growthup.app.data.model.UnlockedRewardEntity
import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.ui.history.HistoryViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryRewardShelfTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeTaskDao: FakeReactiveTaskDao
    private lateinit var fakeRewardDao: FakeRewardDao
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskDao = FakeReactiveTaskDao()
        fakeRewardDao = FakeRewardDao()
        repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)
        viewModel = HistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testHistoryObservesUnlockedRewards() = runTest {
        advanceUntilIdle()
        assertEquals(0, viewModel.uiState.value.unlockedRewards.size)

        fakeRewardDao.insertReward(UnlockedRewardEntity("2026-11-23", "kapibara"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.unlockedRewards.size)
        assertEquals("kapibara", state.unlockedRewards.first().dollId)
    }
}
