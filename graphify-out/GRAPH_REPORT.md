# Graph Report - Growth Up (Updated 2026-11-23)

## Summary
- Total Source Files: 42 Kotlin source files
- Total Core Symbols: 52 classes, entities, composables, & test suites
- Architecture Style: Clean Architecture (Domain, Data, Local Room SQLite, UI / Jetpack Compose Material 3)
- Design Language: Soft Pink Minimalist (WCAG AA Contrast Compliant)
- Performance / Footprint: 1.2 MB Release APK (ProGuard / R8 minified, Zero heavy animation/3D libs)

## Core Knowledge Domains & Hubs

### 1. Reward & Appreciation Subsystem (NEW)
- `DollReward`: Core domain model for collectible doll rewards.
- `DollCatalog`: Deterministic catalog featuring 6 unique cute characters (Kapibara Santai 🦫, Hello Kitty Manis 🐱, Kelinci Ceria 🐰, Teddy Bear Hangat 🧸, Panda Gemoy 🐼, Puppy Shiba Ceria 🐶).
- `UnlockedRewardEntity`: Room table `unlocked_rewards` storing persistent unlocks per date.
- `RewardDao`: Reactive Room DAO providing Flow queries for unlocked dolls.
- `CelebrationDialog()`: Animated Compose pop-up featuring smooth character bounce (`Modifier.offset`) and Canvas confetti.
- `Rak Koleksi Boneka 🎀`: Dedicated showcase shelf in `HistoryScreen` displaying unlocked and locked mystery dolls.

### 2. Task Management & Rollover Subsystem
- `TaskEntity`: Primary Room database entity for daily goals.
- `TaskDao`: Reactive SQLite DAO with Flow observers.
- `TaskRepository` & `TaskRepositoryImpl`: Bridge between local Room data sources and UI layer.
- `TaskEmojiHelper`: Automatic contextual emoji detection (📖 reading, 🏃‍♀️ workout, 🍳 cooking, 💼 work, etc.).

### 3. UI Screen & State Architecture
- `TodayScreen()` & `TodayViewModel`: Day H execution, 100% completion celebration trigger, safe status-bar insets.
- `TomorrowScreen()` & `TomorrowViewModel`: Evening H-1 planning loop.
- `HistoryScreen()` & `HistoryViewModel`: Daily consistency reflection, performance stats, and doll collection shelf.
- `GrowthUpApp()` & `GrowthUpNavHost`: Navigation container.

### 4. Verification Suite (TDD)
- Unit Tests: 100% passing across 9 test suites (`DollCatalogTest`, `RewardDaoFakeTest`, `TaskRepositoryRewardTest`, `CelebrationDialogLogicTest`, `TodayViewModelRewardTest`, `HistoryRewardShelfTest`, `HistorySummaryTest`, `TaskEmojiHelperTest`, `ThemeColorContrastTest`).
- Hardware Verification: Successfully installed and visually verified on TECNO POVA 5 via ADB & Mobile-MCP.
