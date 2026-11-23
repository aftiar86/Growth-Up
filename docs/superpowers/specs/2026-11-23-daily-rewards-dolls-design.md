# Design Document: Daily Completion Rewards & Doll Collection

**Date:** 2026-11-23
**Feature:** Daily Completion Celebration & Collectible Doll Rewards
**Target Audience:** Girlfriend of the user (wants affectionate, cute, and exciting appreciation rewards)

---

## 1. Intent and The Why

The user's girlfriend uses Growth Up to plan and complete her daily tasks. Completing all tasks for the day should feel deeply rewarding, uplifting, and fun. Rather than just a static "100%" text, the app will celebrate with an affectionate pop-up featuring different cute doll characters (such as Kapibara, Hello Kitty, Bunny, Teddy Bear, Panda, Shiba Inu) with celebratory confetti and encouraging praise. Furthermore, each unlocked doll is collected into a "Koleksi Boneka" shelf in the History tab, motivating her to maintain consistency day after day.

---

## 2. Goals, Non-Goals, and Anti-Goals

### Goals
- **Instant Celebration Pop-up:** When all tasks for today are checked (100% completion with at least 1 task), immediately trigger a joyful, pink-themed celebration dialog.
- **Variety of Cute Doll Characters:** Support distinct, lovable characters:
  1. *Kapibara Santai* (Capybara dengan jeruk kecil di kepala 🍊)
  2. *Hello Kitty Manis* (Kucing putih cantik dengan pita pink 🎀)
  3. *Kelinci Ceria* (Kelinci imut memegang bunga 🌸)
  4. *Teddy Bear Hangat* (Beruang memeluk hati pink 🧸)
  5. *Panda Menggemaskan* (Panda lucu melambaikan tangan 🐼)
  6. *Puppy Shiba Ceria* (Anak anjing setia berbandana pink 🐶)
- **Doll Showcase in History Tab:** A cute display shelf showing all dolls she has unlocked across different dates, plus mystery silhouettes for remaining unlockable dolls.
- **Persistent State:** Unlocked rewards are saved in Room Database so they persist across sessions and app restarts.
- **Lightweight & High Performance:** Implemented with lightweight Compose components and vector styling so APK size stays tiny (~1.2 MB) and animations run at 60fps.

### Non-Goals
- Paid gacha / in-app purchase mechanics.
- Complex 3D engine or heavy external asset downloads.

### Anti-Goals
- Annoying spam pop-ups if the user unchecks and re-checks tasks repeatedly on the same day.

---

## 3. Architecture & Data Flow

1. **Model:**
   - `DollReward`: `id`, `name`, `subtitle`, `emoji`, `accentColor`, `message`.
   - `DollCatalog`: Static list of available doll rewards and selector helper based on day/index.
   - `UnlockedRewardEntity`: Room table `unlocked_rewards` (`date` PRIMARY KEY, `dollId`, `unlockedAt`).
2. **Database:**
   - `GrowthUpDatabase` version 2 with `RewardDao`.
3. **UI State:**
   - `TodayUiState` includes `celebrationReward: DollReward?` and `showCelebrationDialog: Boolean`.
   - Triggered when all tasks are completed.
4. **Screens:**
   - `CelebrationDialog`: Animated dialog overlay with bouncing character and confetti.
   - `HistoryScreen`: "Rak Koleksi Boneka" displaying unlocked dolls and unlock dates.
