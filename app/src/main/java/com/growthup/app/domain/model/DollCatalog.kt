package com.growthup.app.domain.model

import java.time.LocalDate

object DollCatalog {

    val SERIES_LIST: List<DollSeries> = listOf(
        DollSeries("seri_hutan", "Sahabat Hutan", "🌲"),
        DollSeries("seri_manis", "Sahabat Manis", "🎀"),
        DollSeries("seri_laut", "Laut & Kutub", "🌊"),
        DollSeries("seri_kebun", "Kebun Bunga", "🌸"),
        DollSeries("seri_fantasi", "Dunia Fantasi", "✨")
    )

    val ALL_DOLLS: List<DollReward> = listOf(
        // Seri 1: Sahabat Hutan & Alam 🌲
        DollReward(
            id = "kapibara",
            name = "Kapibara Santai",
            subtitle = "Paling tenang & santuy 🍊",
            emoji = "🦫",
            accentColorHex = 0xFFD88373,
            praiseMessage = "Hebat banget sayang! Hari ini semua target selesai dengan tenang dan luar biasa! 🍊✨",
            seriesId = "seri_hutan"
        ),
        DollReward(
            id = "panda",
            name = "Panda Gemoy",
            subtitle = "Lucu & menggemaskan 🐼",
            emoji = "🐼",
            accentColorHex = 0xFF81B29A,
            praiseMessage = "Panda gemes tepuk tangan buat kamu! Keren banget hari ini 100% tuntas! 🐼🎋",
            seriesId = "seri_hutan"
        ),
        DollReward(
            id = "teddy_bear",
            name = "Teddy Bear Hangat",
            subtitle = "Pelukan hangat untukmu 🧸",
            emoji = "🧸",
            accentColorHex = 0xFFE07A5F,
            praiseMessage = "Peluk hangat buat kamu yang sudah berjuang seharian. Istirahat yang nyenyak ya sayang! 🧸💕",
            seriesId = "seri_hutan"
        ),
        DollReward(
            id = "koala",
            name = "Koala Manja",
            subtitle = "Tukang rebahan produktif 🌿",
            emoji = "🐨",
            accentColorHex = 0xFF9E8279,
            praiseMessage = "Kamu hebat banget! Setelah produktif seharian, sekarang waktunya peluk guling & santai! 🐨🌿",
            seriesId = "seri_hutan"
        ),
        DollReward(
            id = "rubah",
            name = "Rubah Manis",
            subtitle = "Cerdas dan anggun 🍂",
            emoji = "🦊",
            accentColorHex = 0xFFE76F51,
            praiseMessage = "Kecerdasan dan ketekunanmu hari ini luar biasa menginspirasi sayang! 🦊🍁",
            seriesId = "seri_hutan"
        ),
        DollReward(
            id = "rusa",
            name = "Rusa Anggun",
            subtitle = "Lembut dan menenangkan 🍃",
            emoji = "🦌",
            accentColorHex = 0xFFC97A5E,
            praiseMessage = "Langkahmu anggun dan konsisten. Setiap harimu selalu penuh pesona cantik! 🦌🌸",
            seriesId = "seri_hutan"
        ),

        // Seri 2: Sahabat Manis & Anggun 🎀
        DollReward(
            id = "hello_kitty",
            name = "Hello Kitty Manis",
            subtitle = "Imut dan anggun 🎀",
            emoji = "🐱",
            accentColorHex = 0xFFFF85A1,
            praiseMessage = "Aaaa lucunya pacarku! Kamu keren banget berhasil bereskan semua rencana hari ini! 🎀🌸",
            seriesId = "seri_manis"
        ),
        DollReward(
            id = "kelinci",
            name = "Kelinci Ceria",
            subtitle = "Lompat riang gembira 🌸",
            emoji = "🐰",
            accentColorHex = 0xFFF4A261,
            praiseMessage = "Semangatmu manis sekali! Kelinci kecil ini bangga banget sama pencapaianmu hari ini! 🌸💖",
            seriesId = "seri_manis"
        ),
        DollReward(
            id = "kucing_anggora",
            name = "Kucing Anggora",
            subtitle = "Bulu putih cantik jelita 🤍",
            emoji = "🐈",
            accentColorHex = 0xFFDDA15E,
            praiseMessage = "Cantik, mandiri, dan pintar! Kamu selalu bikin aku bangga setiap hari! 🐈✨",
            seriesId = "seri_manis"
        ),
        DollReward(
            id = "corgi",
            name = "Puppy Corgi Gemoy",
            subtitle = "Bokong bulat jalan riang 🐾",
            emoji = "🐕",
            accentColorHex = 0xFFE9C46A,
            praiseMessage = "Guk guk! Senang banget lihat kamu seproduktif ini hari ini! Proud of you sayang! 🐕🌟",
            seriesId = "seri_manis"
        ),
        DollReward(
            id = "kucing_belang",
            name = "Kucing Belang Manja",
            subtitle = "Suka mendengkur bahagia 🧶",
            emoji = "🐾",
            accentColorHex = 0xFFE07A5F,
            praiseMessage = "Meoww~ Semua rencana sudah beres dengan sempurna! Kamu pantas dapat peluk erat! 🐾💕",
            seriesId = "seri_manis"
        ),
        DollReward(
            id = "puppy_poodle",
            name = "Puppy Poodle Modis",
            subtitle = "Pintar dan modis bergaya 💖",
            emoji = "🐩",
            accentColorHex = 0xFFFF758F,
            praiseMessage = "Gaya kerjamu hari ini rapi dan memukau! Poodle mungil ini ikut bahagia! 🐩🎀",
            seriesId = "seri_manis"
        ),

        // Seri 3: Laut & Kutub Ceria 🌊
        DollReward(
            id = "penguin",
            name = "Penguin Kutub Lucu",
            subtitle = "Jalan geal-geol imut ❄️",
            emoji = "🐧",
            accentColorHex = 0xFF457B9D,
            praiseMessage = "Waddle waddle~ Penguin kecil kagum banget sama konsistensi hebatmu hari ini! 🐧❄️",
            seriesId = "seri_laut"
        ),
        DollReward(
            id = "berang_berang",
            name = "Berang-berang Ceria",
            subtitle = "Pegang tangan di air 🌊",
            emoji = "🦦",
            accentColorHex = 0xFF836953,
            praiseMessage = "Selalu bergandengan tangan menemanimu tumbuh makin hebat setiap hari! 🦦💧",
            seriesId = "seri_laut"
        ),
        DollReward(
            id = "lumba_lumba",
            name = "Lumba-lumba Ramah",
            subtitle = "Lompat ceria di lautan 💦",
            emoji = "🐬",
            accentColorHex = 0xFF2A9D8F,
            praiseMessage = "Splash! Hari ini kamu luar biasa bersinar bagai lompatan lumba-lumba ceria! 🐬💙",
            seriesId = "seri_laut"
        ),
        DollReward(
            id = "anjing_laut",
            name = "Anjing Laut Imut",
            subtitle = "Tepuk tangan senang 🫧",
            emoji = "🦭",
            accentColorHex = 0xFF718096,
            praiseMessage = "Plok plok plok! Tepuk tangan paling meriah untuk pacarku tersayang yang juara! 🦭🤍",
            seriesId = "seri_laut"
        ),
        DollReward(
            id = "paus_biru",
            name = "Paus Biru Manis",
            subtitle = "Hati besar penuh cinta 💙",
            emoji = "🐳",
            accentColorHex = 0xFF3D5A80,
            praiseMessage = "Cintaku dan rasa banggaku padamu hari ini seluas dan sedalam samudera! 🐳🌊",
            seriesId = "seri_laut"
        ),
        DollReward(
            id = "kura_kura",
            name = "Kura-kura Bijak",
            subtitle = "Pelan tapi pasti sukses 🐚",
            emoji = "🐢",
            accentColorHex = 0xFF52796F,
            praiseMessage = "Pelan tapi pasti, konsistensimu yang tenang terbukti membawa hasil luar biasa! 🐢🌱",
            seriesId = "seri_laut"
        ),

        // Seri 4: Kebun Bunga & Hewan Kecil 🌸
        DollReward(
            id = "hamster",
            name = "Hamster Bakpao",
            subtitle = "Pipi bulat makan kuaci 🌻",
            emoji = "🐹",
            accentColorHex = 0xFFF39A59,
            praiseMessage = "Pipi gembul hamster ini tersenyum manis melihat kerja kerasmu yang tuntas! 🐹🌻",
            seriesId = "seri_kebun"
        ),
        DollReward(
            id = "tupai",
            name = "Tupai Lincah",
            subtitle = "Kolektor kacang rajin 🌰",
            emoji = "🐿️",
            accentColorHex = 0xFFB06D3B,
            praiseMessage = "Rajin dan cekatan! Satu per satu target hari ini sukses kamu kumpulkan! 🐿️🍂",
            seriesId = "seri_kebun"
        ),
        DollReward(
            id = "anak_ayam",
            name = "Anak Ayam Kuning",
            subtitle = "Piyik-piyik ceria pagi ☀️",
            emoji = "🐥",
            accentColorHex = 0xFFF4D03F,
            praiseMessage = "Piyik piyik! Semangat manismu selalu menerangi hari dengan kebahagiaan! 🐥✨",
            seriesId = "seri_kebun"
        ),
        DollReward(
            id = "burung_pipit",
            name = "Burung Pipit Manis",
            subtitle = "Kicau merdu bawa damai 🌿",
            emoji = "🐦",
            accentColorHex = 0xFF588157,
            praiseMessage = "Kicauan merdu hari ini merayakan keberhasilanmu yang penuh dedikasi! 🐦🎶",
            seriesId = "seri_kebun"
        ),
        DollReward(
            id = "kupu_kupu",
            name = "Kupu-kupu Pelangi",
            subtitle = "Cantik terbang bebas 🌺",
            emoji = "🦋",
            accentColorHex = 0xFFB56576,
            praiseMessage = "Kamu sedang bertumbuh dan mekar menjadi versi dirimu yang paling mempesona! 🦋🌸",
            seriesId = "seri_kebun"
        ),
        DollReward(
            id = "landak",
            name = "Landak Mini Gemas",
            subtitle = "Duri mungil hati lembut 🍄",
            emoji = "🦔",
            accentColorHex = 0xFF8A5A44,
            praiseMessage = "Mungil, manis, dan kuat! Kamu selalu sanggup menghadapi hari dengan senyuman! 🦔💕",
            seriesId = "seri_kebun"
        ),

        // Seri 5: Dunia Fantasi & Dongeng ✨
        DollReward(
            id = "unicorn",
            name = "Unicorn Ajaib",
            subtitle = "Bintang pelangi impian 🌈",
            emoji = "🦄",
            accentColorHex = 0xFFC77DFF,
            praiseMessage = "Ajaib dan berkilau! Hari ini kamu membuktikan bahwa mimpimu selalu bisa dicapai! 🦄✨",
            seriesId = "seri_fantasi"
        ),
        DollReward(
            id = "peri_bintang",
            name = "Peri Bintang Kecil",
            subtitle = "Menebar serbuk kebaikan ✨",
            emoji = "🧚‍♀️",
            accentColorHex = 0xFFFFB703,
            praiseMessage = "Taburan bintang berkilau untuk pacarku yang hatinya paling tulus dan pekerja keras! 🧚‍♀️⭐",
            seriesId = "seri_fantasi"
        ),
        DollReward(
            id = "naga_mini",
            name = "Naga Mini Gemoy",
            subtitle = "Napas gelembung cinta 🔥",
            emoji = "🐲",
            accentColorHex = 0xFF6A994E,
            praiseMessage = "Kekuatanmu mengagumkan! Tantangan apa pun hari ini ludes kamu taklukkan! 🐲💚",
            seriesId = "seri_fantasi"
        ),
        DollReward(
            id = "kucing_bulan",
            name = "Kucing Bulan Manja",
            subtitle = "Menemani tidur nyenyakmu 🌌",
            emoji = "🌙",
            accentColorHex = 0xFF577590,
            praiseMessage = "Bulan tersenyum menemanimu. Sekarang tutup mata dan tidurlah dengan bangga sayang! 🌙💤",
            seriesId = "seri_fantasi"
        ),
        DollReward(
            id = "flamingo",
            name = "Flamingo Cantik",
            subtitle = "Pesona pink mempesona 🌺",
            emoji = "🦩",
            accentColorHex = 0xFFF28482,
            praiseMessage = "Berdiri anggun dengan percaya diri tinggi. Kamu adalah keindahan terindah! 🦩💖",
            seriesId = "seri_fantasi"
        ),
        DollReward(
            id = "angsa_putih",
            name = "Angsa Putih Ratu",
            subtitle = "Anggun memesona dunia 👑",
            emoji = "🦢",
            accentColorHex = 0xFF7D8597,
            praiseMessage = "Sempurna dan memesona! Terima kasih sudah selalu berusaha memberikan yang terbaik! 🦢👑",
            seriesId = "seri_fantasi"
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
