package com.growthup.app

import com.growthup.app.data.model.TaskEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class TaskEntityTest {

    @Test
    fun testTaskEntityDefaultValues() {
        val today = LocalDate.now().toString()
        val task = TaskEntity(
            title = "Membaca buku 15 menit",
            targetDate = today
        )

        assertEquals(0L, task.id)
        assertEquals("Membaca buku 15 menit", task.title)
        assertEquals(today, task.targetDate)
        assertFalse("Task should not be completed by default", task.isCompleted)
        assertFalse("Task should not be archived by default", task.isArchived)
        assertTrue("Created timestamp should be valid", task.createdAt > 0)
    }

    @Test
    fun testTaskCompletionCopy() {
        val task = TaskEntity(
            id = 1L,
            title = "Olahraga pagi",
            targetDate = "2026-11-22"
        )
        val completedTask = task.copy(isCompleted = true)
        assertTrue(completedTask.isCompleted)
        assertEquals(1L, completedTask.id)
    }
}
