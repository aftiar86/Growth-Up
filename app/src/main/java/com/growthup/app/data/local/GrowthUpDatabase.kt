package com.growthup.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.data.model.UnlockedRewardEntity

@Database(
    entities = [TaskEntity::class, UnlockedRewardEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GrowthUpDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: GrowthUpDatabase? = null

        fun getDatabase(context: Context): GrowthUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GrowthUpDatabase::class.java,
                    "growth_up_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
