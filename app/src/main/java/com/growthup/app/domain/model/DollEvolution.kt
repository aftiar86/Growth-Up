package com.growthup.app.domain.model

data class DollSeries(
    val id: String,
    val title: String,
    val icon: String
)

data class DollEvolution(
    val dollId: String,
    val unlockCount: Int,
    val starLevel: Int,
    val accessoryEmoji: String?,
    val titleBadge: String
)

object DollEvolutionCalculator {
    fun calculateEvolution(dollId: String, totalUnlocks: Int): DollEvolution {
        return when {
            totalUnlocks <= 0 -> DollEvolution(
                dollId = dollId,
                unlockCount = 0,
                starLevel = 0,
                accessoryEmoji = null,
                titleBadge = "Terkunci"
            )
            totalUnlocks == 1 -> DollEvolution(
                dollId = dollId,
                unlockCount = 1,
                starLevel = 1,
                accessoryEmoji = null,
                titleBadge = "⭐ Sahabat Baru"
            )
            totalUnlocks == 2 -> DollEvolution(
                dollId = dollId,
                unlockCount = 2,
                starLevel = 2,
                accessoryEmoji = "🎀",
                titleBadge = "⭐⭐ Sahabat Setia"
            )
            else -> DollEvolution(
                dollId = dollId,
                unlockCount = totalUnlocks,
                starLevel = 3,
                accessoryEmoji = "👑",
                titleBadge = "⭐⭐⭐ Boneka Ratu"
            )
        }
    }
}
