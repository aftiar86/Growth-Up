package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DollCatalogTest {

    @Test
    fun testCatalogHasThirtyDistinctDolls() {
        val dolls = DollCatalog.ALL_DOLLS
        assertEquals(30, dolls.size)
        val ids = dolls.map { it.id }.toSet()
        assertEquals(30, ids.size)
        assertEquals(5, DollCatalog.SERIES_LIST.size)
    }

    @Test
    fun testCatalogContainsKapibaraAndHelloKitty() {
        val kapibara = DollCatalog.getDollById("kapibara")
        assertNotNull(kapibara)
        assertTrue(kapibara!!.name.contains("Kapibara"))

        val kitty = DollCatalog.getDollById("hello_kitty")
        assertNotNull(kitty)
        assertTrue(kitty!!.name.contains("Hello Kitty"))
    }

    @Test
    fun testGetRewardForDateIsDeterministicAndRotates() {
        val reward1 = DollCatalog.getRewardForDate("2026-11-23")
        val reward2 = DollCatalog.getRewardForDate("2026-11-23")
        assertEquals(reward1.id, reward2.id)

        val rewardNextDay = DollCatalog.getRewardForDate("2026-11-24")
        assertNotNull(rewardNextDay)
    }
}
