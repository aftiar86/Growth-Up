package com.growthup.app.domain.model

data class DollReward(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val accentColorHex: Long,
    val praiseMessage: String
)
