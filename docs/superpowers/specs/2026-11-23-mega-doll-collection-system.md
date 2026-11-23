# Design Document: Mega Doll Collection & Star Evolution System (30 Dolls, 5 Series, 3 Star Tiers)

**Date:** 2026-11-23  
**Feature:** 30 Unique Dolls, 5 Themed Series, and 3-Star Accessory Evolution System  
**Target Audience:** User's girlfriend (exciting long-term motivation, cute progression, non-repetitive daily delights)

---

## 1. Intent & Goals

Transform the daily celebration and doll collection into an engaging, long-lasting progression system combining:
1. **30 Unique Dolls**: A full monthly calendar of distinct lovable characters so she never gets the same doll within a 30-day window.
2. **5 Themed Series**: Dolls are organized into charming thematic albums:
   - **Seri 1: Sahabat Hutan & Alam 🌲** (Kapibara, Panda, Teddy Bear, Koala, Rubah Manis, Rusa Anggun)
   - **Seri 2: Sahabat Manis & Anggun 🎀** (Hello Kitty, Kelinci Ceria, Kucing Anggora, Corgi Menggemaskan, Kucing Belang, Puppy Poodle)
   - **Seri 3: Laut & Kutub Ceria 🌊** (Penguin Kutub, Berang-berang Ceria, Lumba-lumba, Anjing Laut, Paus Biru, Kura-kura Bijak)
   - **Seri 4: Hewan Kecil & Kebun Bunga 🌸** (Hamster Bakpao, Tupai Ceria, Anak Ayam Kuning, Burung Pipit, Kupu-kupu Pelangi, Landak Mini)
   - **Seri 5: Negeri Dongeng Fantasi ✨** (Unicorn Pelangi, Peri Bintang, Naga Mini Gemoy, Kucing Bulan, Flamingo Cantik, Angsa Putih)
3. **Star Accessory Evolution (Level 1-3)**:
   - When a doll is unlocked for the first time: **⭐ Bintang 1 (Sahabat Baru)**
   - When unlocked a 2nd time: **⭐⭐ Bintang 2 (Aksesoris Pita Cantik 🎀)**
   - When unlocked 3+ times: **⭐⭐⭐ Bintang 3 (Mahkota Ratu Cantik 👑)**
   - Every day is rewarding: even when an existing doll is encountered, it levels up with a special celebration!

---

## 2. Architecture & Data Flow

1. **Domain Model:**
   - `DollSeries`: Enum / data class for the 5 series with title, icon, and description.
   - `DollReward`: `id`, `name`, `subtitle`, `emoji`, `accentColorHex`, `praiseMessage`, `seriesId`.
   - `DollCatalog`: 30 dolls mapped across 5 series. Deterministic date rotation based on day of month (`dayOfYear % 30`).
2. **Evolution Logic (`DollEvolutionCalculator`):**
   - Given a `dollId` and the user's `List<UnlockedRewardEntity>`, count how many times it was unlocked.
   - `level = count.coerceIn(1, 3)`
   - Accessory badge: Level 1 = none, Level 2 = 🎀 Pita, Level 3 = 👑 Mahkota.
3. **UI Updates:**
   - `CelebrationDialog`: Shows star tier badge (`⭐`, `⭐⭐ Naik Level!`, `⭐⭐⭐ Mahkota Ratu!`) with celebratory badge.
   - `HistoryScreen`:
     - Series filter / tabs to switch between the 5 thematic albums.
     - Doll cards show star ratings (`⭐⭐⭐`), accessory overlay (🎀/👑), and unlock counters.
     - Overall progress banner: `${unlockedDollsCount}/30 Boneka Terkumpul 💕`.

---

## 3. Constraints & Ponytail Guidelines

- **Zero External Heavy Libs**: Keep APK ~1.2 MB.
- **Fast Room Queries**: Compute star counts efficiently using Room Flow.
- **Backwards Compatibility**: Existing `unlocked_rewards` table and records are fully preserved.
