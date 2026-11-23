package com.growthup.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.growthup.app.data.model.UnlockedRewardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM unlocked_rewards ORDER BY date DESC")
    fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>>

    @Query("SELECT * FROM unlocked_rewards WHERE date = :date LIMIT 1")
    suspend fun getRewardForDate(date: String): UnlockedRewardEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReward(reward: UnlockedRewardEntity): Long
}
