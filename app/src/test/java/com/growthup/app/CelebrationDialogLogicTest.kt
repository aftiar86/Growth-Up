package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CelebrationDialogLogicTest {

    @Test
    fun testPraiseMessageHasAffectionateTone() {
        DollCatalog.ALL_DOLLS.forEach { doll ->
            assertFalse("Praise message should not be blank", doll.praiseMessage.isBlank())
            assertTrue("Emoji should not be blank", doll.emoji.isNotBlank())
            assertTrue("Subtitle should not be blank", doll.subtitle.isNotBlank())
            assertTrue("Accent color should be valid hex", doll.accentColorHex != 0L)
        }
    }
}
