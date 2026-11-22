package com.growthup.app

import com.growthup.app.notification.NotificationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationHelperTest {

    @Test
    fun testCalculateNextTriggerTimeReturnsFuture() {
        val now = System.currentTimeMillis()
        val triggerTime = NotificationHelper.calculateNextTriggerTime(
            targetHour = 20,
            targetMinute = 0,
            currentTimeMillis = now
        )

        assertTrue("Trigger time $triggerTime must be in the future relative to $now", triggerTime > now)

        val diffHours = (triggerTime - now) / (1000 * 60 * 60.0)
        assertTrue("Trigger time must be within next 24 hours ($diffHours hrs)", diffHours <= 24.01)
    }

    @Test
    fun testChannelConfigurationConstants() {
        assertEquals("growth_up_channel", NotificationHelper.CHANNEL_ID)
        assertEquals("Pengingat Growth Up", NotificationHelper.CHANNEL_NAME)
    }
}
