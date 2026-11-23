package com.growthup.app.domain.model

import java.time.LocalDate

object DollCatalog {
    val ALL_DOLLS: List<DollReward> = listOf(
        DollReward(
            id = "kapibara",
            name = "Kapibara Santai",
            subtitle = "Paling tenang & santuy 🍊",
            emoji = "🦫",
            accentColorHex = 0xFFD88373,
            praiseMessage = "Hebat banget sayang! Hari ini semua target selesai dengan tenang dan luar biasa! 🍊✨"
        ),
        DollReward(
            id = "hello_kitty",
            name = "Hello Kitty Manis",
            subtitle = "Imut dan anggun 🎀",
            emoji = "🐱",
            accentColorHex = 0xFFFF85A1,
            praiseMessage = "Aaaa lucunya pacarku! Kamu keren banget berhasil bereskan semua rencana hari ini! 🎀🌸"
        ),
        DollReward(
            id = "kelinci",
            name = "Kelinci Ceria",
            subtitle = "Lompat riang gembira 🌸",
            emoji = "🐰",
            accentColorHex = 0xFFF4A261,
            praiseMessage = "Semangatmu manis sekali! Kelinci kecil ini bangga banget sama pencapaianmu hari ini! 🌸💖"
        ),
        DollReward(
            id = "teddy_bear",
            name = "Teddy Bear Hangat",
            subtitle = "Pelukan hangat untukmu 🧸",
            emoji = "🧸",
            accentColorHex = 0xFFE07A5F,
            praiseMessage = "Peluk hangat buat kamu yang sudah berjuang seharian. Istirahat yang nyenyak ya sayang! 🧸💕"
        ),
        DollReward(
            id = "panda",
            name = "Panda Gemoy",
            subtitle = "Lucu & menggemaskan 🐼",
            emoji = "🐼",
            accentColorHex = 0xFF81B29A,
            praiseMessage = "Panda gemes tepuk tangan buat kamu! Keren banget hari ini 100% tuntas! 🐼🎋"
        ),
        DollReward(
            id = "shiba",
            name = "Puppy Shiba Ceria",
            subtitle = "Setia menemani harimu 🐶",
            emoji = "🐶",
            accentColorHex = 0xFFE9C46A,
            praiseMessage = "Guk guk! Senang banget lihat kamu seproduktif ini hari ini! You did it, proud of you! 🐶🌟"
        )
    )

    fun getDollById(id: String): DollReward? {
        return ALL_DOLLS.find { it.id == id }
    }

    fun getRewardForDate(dateString: String): DollReward {
        val dayIndex = try {
            val date = LocalDate.parse(dateString)
            (date.toEpochDay() % ALL_DOLLS.size).toInt().let { if (it < 0) it + ALL_DOLLS.size else it }
        } catch (e: Exception) {
            (dateString.hashCode() % ALL_DOLLS.size).let { if (it < 0) it + ALL_DOLLS.size else it }
        }
        return ALL_DOLLS[dayIndex]
    }
}
