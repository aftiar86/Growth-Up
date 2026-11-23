package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import com.growthup.app.domain.model.DollEvolutionCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DollEvolutionTest {

    @Test
    fun testCatalogHasThirtyDistinctDollsInFiveSeries() {
        val dolls = DollCatalog.ALL_DOLLS
        assertEquals(30, dolls.size)
        assertEquals(30, dolls.map { it.id }.toSet().size)
        assertEquals(5, DollCatalog.SERIES_LIST.size)
    }

    @Test
    fun testEvolutionCalculatorTiers() {
        val level0 = DollEvolutionCalculator.calculateEvolution("kapibara", 0)
        assertEquals(0, level0.starLevel)
        assertNull(level0.accessoryEmoji)

        val level1 = DollEvolutionCalculator.calculateEvolution("kapibara", 1)
        assertEquals(1, level1.starLevel)
        assertNull(level1.accessoryEmoji)
        assertEquals("⭐ Sahabat Baru", level1.titleBadge)

        val level2 = DollEvolutionCalculator.calculateEvolution("kapibara", 2)
        assertEquals(2, level2.starLevel)
        assertEquals("🎀", level2.accessoryEmoji)
        assertEquals("⭐⭐ Sahabat Setia", level2.titleBadge)

        val level3 = DollEvolutionCalculator.calculateEvolution("kapibara", 5)
        assertEquals(3, level3.starLevel)
        assertEquals("👑", level3.accessoryEmoji)
        assertEquals("⭐⭐⭐ Boneka Ratu", level3.titleBadge)
    }
}
