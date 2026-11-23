package com.growthup.app

import com.growthup.app.data.repository.TaskRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskRepositoryRewardTest {

    @Test
    fun testUnlockRewardAndRetrieve() = runBlocking {
        val fakeTaskDao = FakeReactiveTaskDao()
        val fakeRewardDao = FakeRewardDao()
        val repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)

        val date = "2026-11-23"
        val unlocked = repository.unlockRewardForDate(date, "kapibara")
        assertTrue(unlocked)

        val retrieved = repository.getRewardForDate(date)
        assertNotNull(retrieved)
        assertEquals("kapibara", retrieved?.dollId)

        // Attempting to unlock again on the same day should return false (already unlocked)
        val secondAttempt = repository.unlockRewardForDate(date, "hello_kitty")
        assertFalse(secondAttempt)

        val allRewards = repository.getAllUnlockedRewards().first()
        assertEquals(1, allRewards.size)
    }
}
