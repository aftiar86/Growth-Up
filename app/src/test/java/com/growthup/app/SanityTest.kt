package com.growthup.app

import org.junit.Assert.assertEquals
import org.junit.Test

class SanityTest {
    @Test
    fun testEnvironmentSanity() {
        assertEquals("Growth Up", "Growth " + "Up")
    }
}
