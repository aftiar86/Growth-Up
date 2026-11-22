package com.growthup.app

import com.growthup.app.data.model.TaskEntity
import com.growthup.app.ui.history.HistoryCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class HistorySummaryTest {

    @Test
    fun testHistoryStatisticsCalculation() {
        val sampleTasks = listOf(
            TaskEntity(id = 1, title = "Task 1", targetDate = "2026-11-20", isCompleted = true),
            TaskEntity(id = 2, title = "Task 2", targetDate = "2026-11-20", isCompleted = true),
            TaskEntity(id = 3, title = "Task 3", targetDate = "2026-11-21", isCompleted = false),
            TaskEntity(id = 4, title = "Task 4", targetDate = "2026-11-21", isCompleted = true)
        )

        val stats = HistoryCalculator.calculateStats(sampleTasks)

        assertEquals(4, stats.totalCount)
        assertEquals(3, stats.completedCount)
        assertEquals(75, stats.percentage)
        assertEquals(2, stats.daysTracked)
    }

    @Test
    fun testEmptyHistoryStatistics() {
        val stats = HistoryCalculator.calculateStats(emptyList())

        assertEquals(0, stats.totalCount)
        assertEquals(0, stats.completedCount)
        assertEquals(0, stats.percentage)
        assertEquals(0, stats.daysTracked)
    }
}
