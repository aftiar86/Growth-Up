package com.growthup.app

import com.growthup.app.data.local.RewardDao
import com.growthup.app.data.model.UnlockedRewardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FakeRewardDao : RewardDao {
    private val rewards = MutableStateFlow<Map<String, UnlockedRewardEntity>>(emptyMap())

    override fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>> {
        return rewards.map { it.values.sortedByDescending { r -> r.date } }
    }

    override suspend fun getRewardForDate(date: String): UnlockedRewardEntity? {
        return rewards.value[date]
    }

    override suspend fun insertReward(reward: UnlockedRewardEntity): Long {
        if (rewards.value.containsKey(reward.date)) return -1L
        rewards.value = rewards.value + (reward.date to reward)
        return 1L
    }
}

class RewardDaoFakeTest {
    @Test
    fun testInsertAndRetrieveReward() = runBlocking {
        val dao = FakeRewardDao()
        assertNull(dao.getRewardForDate("2026-11-23"))

        val entity = UnlockedRewardEntity(date = "2026-11-23", dollId = "kapibara")
        val insertResult = dao.insertReward(entity)
        assertEquals(1L, insertResult)

        val retrieved = dao.getRewardForDate("2026-11-23")
        assertNotNull(retrieved)
        assertEquals("kapibara", retrieved?.dollId)

        // Ignore duplicate insert on same date
        val duplicateInsert = dao.insertReward(entity)
        assertEquals(-1L, duplicateInsert)

        val list = dao.getAllUnlockedRewards().first()
        assertEquals(1, list.size)
    }
}
