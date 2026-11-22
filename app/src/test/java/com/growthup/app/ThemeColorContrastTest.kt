package com.growthup.app

import com.growthup.app.ui.theme.GrowthUpColors
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeColorContrastTest {

    private fun calculateLuminance(r: Int, g: Int, b: Int): Double {
        fun channel(c: Int): Double {
            val s = c / 255.0
            return if (s <= 0.03928) s / 12.92 else Math.pow((s + 0.055) / 1.055, 2.4)
        }
        return 0.2126 * channel(r) + 0.7152 * channel(g) + 0.0722 * channel(b)
    }

    private fun contrastRatio(fg: Long, bg: Long): Double {
        val r1 = ((fg shr 16) and 0xFF).toInt()
        val g1 = ((fg shr 8) and 0xFF).toInt()
        val b1 = (fg and 0xFF).toInt()

        val r2 = ((bg shr 16) and 0xFF).toInt()
        val g2 = ((bg shr 8) and 0xFF).toInt()
        val b2 = (bg and 0xFF).toInt()

        val l1 = calculateLuminance(r1, g1, b1)
        val l2 = calculateLuminance(r2, g2, b2)

        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }

    @Test
    fun testTextCharcoalContrastOnBackground() {
        // WCAG AA requirement for normal body text is at least 4.5:1
        val ratioOnBg = contrastRatio(GrowthUpColors.TextCharcoalHex, GrowthUpColors.BackgroundCreamHex)
        assertTrue("Text on background contrast $ratioOnBg must be >= 4.5", ratioOnBg >= 4.5)

        val ratioOnCard = contrastRatio(GrowthUpColors.TextCharcoalHex, GrowthUpColors.SurfaceWhiteHex)
        assertTrue("Text on card surface contrast $ratioOnCard must be >= 4.5", ratioOnCard >= 4.5)
    }
}
