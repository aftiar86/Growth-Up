package com.growthup.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlocked_rewards")
data class UnlockedRewardEntity(
    @PrimaryKey
    val date: String,
    val dollId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)
