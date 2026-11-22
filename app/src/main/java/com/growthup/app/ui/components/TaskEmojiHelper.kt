package com.growthup.app.ui.components

object TaskEmojiHelper {
    fun getEmojiForTask(title: String): String {
        val lower = title.lowercase()
        return when {
            lower.contains("buku") || lower.contains("baca") || lower.contains("novel") || lower.contains("komik") -> "📖"
            lower.contains("olahraga") || lower.contains("lari") || lower.contains("gym") || lower.contains("workout") || lower.contains("senam") || lower.contains("jogging") -> "🏃‍♀️"
            lower.contains("yoga") || lower.contains("meditasi") || lower.contains("stretching") -> "🧘‍♀️"
            lower.contains("belajar") || lower.contains("kuliah") || lower.contains("tugas") || lower.contains("skripsi") || lower.contains("kursus") -> "📚"
            lower.contains("kerja") || lower.contains("kantor") || lower.contains("meeting") || lower.contains("zoom") || lower.contains("laptop") || lower.contains("coding") -> "💻"
            lower.contains("makan") || lower.contains("sarapan") || lower.contains("siang") || lower.contains("malam") || lower.contains("dinner") || lower.contains("masak") -> "🥗"
            lower.contains("minum") || lower.contains("air") || lower.contains("kopi") || lower.contains("teh") || lower.contains("juice") -> "☕"
            lower.contains("belanja") || lower.contains("beli") || lower.contains("shopping") || lower.contains("mall") || lower.contains("pasar") -> "🛍️"
            lower.contains("tidur") || lower.contains("istirahat") || lower.contains("nap") || lower.contains("rebahan") -> "🌙"
            lower.contains("mandi") || lower.contains("skincare") || lower.contains("masker") || lower.contains("perawatan") -> "🧴"
            lower.contains("ibadah") || lower.contains("sholat") || lower.contains("doa") || lower.contains("ngaji") || lower.contains("gereja") -> "✨"
            lower.contains("bersih") || lower.contains("rapi") || lower.contains("cuci") || lower.contains("jemur") || lower.contains("setrika") -> "🧹"
            lower.contains("jalan") || lower.contains("liburan") || lower.contains("healing") || lower.contains("nonton") || lower.contains("film") || lower.contains("bioskop") -> "🎬"
            lower.contains("teman") || lower.contains("hangout") || lower.contains("kencan") || lower.contains("curhat") -> "👭"
            lower.contains("bunga") || lower.contains("tanaman") || lower.contains("siram") -> "🌸"
            lower.contains("uang") || lower.contains("nabung") || lower.contains("bayar") || lower.contains("kasir") || lower.contains("transfer") -> "💸"
            else -> "🌸"
        }
    }
}
