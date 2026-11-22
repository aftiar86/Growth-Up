package com.growthup.app

import com.growthup.app.ui.components.TaskEmojiHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskEmojiHelperTest {

    @Test
    fun testBookEmoji() {
        assertEquals("📖", TaskEmojiHelper.getEmojiForTask("Membaca buku 20 halaman"))
        assertEquals("📖", TaskEmojiHelper.getEmojiForTask("Beli novel baru"))
    }

    @Test
    fun testWorkoutEmoji() {
        assertEquals("🏃‍♀️", TaskEmojiHelper.getEmojiForTask("Olahraga pagi keliling kompleks"))
        assertEquals("🏃‍♀️", TaskEmojiHelper.getEmojiForTask("Lari 5km"))
        assertEquals("🧘‍♀️", TaskEmojiHelper.getEmojiForTask("Yoga sore"))
    }

    @Test
    fun testDefaultEmoji() {
        assertEquals("🌸", TaskEmojiHelper.getEmojiForTask("Kegiatan rahasia"))
    }
}
