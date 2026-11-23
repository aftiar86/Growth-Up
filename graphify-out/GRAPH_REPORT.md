# Graph Report - Growth Up (Updated 2026-11-23 - Mega Doll System)

## Summary
- Total Source Files: 44 Kotlin source files
- Total Collectible Dolls: 30 unique characters across 5 thematic series
- Star Evolution Tiers: 3 levels (⭐ Sahabat Baru, ⭐⭐ Sahabat Setia + 🎀 Pita, ⭐⭐⭐ Boneka Ratu + 👑 Mahkota)
- Architecture Style: Clean Architecture (Domain, Data, Local Room SQLite, UI / Jetpack Compose Material 3)
- Performance / Footprint: 1.22 MB Release APK (ProGuard / R8 minified, Zero external animation/3D libs)

## Core Knowledge Domains & Hubs

### 1. Mega Doll Collection & Star Evolution Subsystem
- `DollSeries`: 5 thematic albums (Sahabat Hutan 🌲, Sahabat Manis 🎀, Laut & Kutub 🌊, Kebun Bunga 🌸, Dunia Fantasi ✨).
- `DollCatalog`: 30 unique dolls with affectionate custom praise copy.
- `DollEvolution`: Domain model representing a doll's star rating and accessory tier.
- `DollEvolutionCalculator`: Pure domain engine mapping unlock frequency to 3 evolution tiers.
- `CelebrationDialog()`: Animated Compose dialog with bouncing avatar, dynamic accessory overlay, and star rating badge.
- `HistoryScreen()` (Rak Koleksi Boneka): Interactive series filter chips and star rating progression cards with unlock frequency tallies.

### 2. Task Management & Rollover Subsystem
- `TaskEntity`: Primary Room database entity for daily goals.
- `TaskDao`: Reactive SQLite DAO with Flow observers.
- `TaskRepository` & `TaskRepositoryImpl`: Bridges local Room data sources and UI layer.
- `TaskEmojiHelper`: Contextual emoji detection for everyday activities.

### 3. UI Screen & State Architecture
- `TodayScreen()` & `TodayViewModel`: Day H execution, 100% completion celebration trigger, safe status-bar insets.
- `TomorrowScreen()` & `TomorrowViewModel`: Evening H-1 planning loop.
- `HistoryScreen()` & `HistoryViewModel`: Reflection statistics, series filtering, and doll collection showcase.
- `GrowthUpApp()` & `GrowthUpNavHost`: App navigation scaffold.

### 4. Verification Suite (TDD)
- Unit Tests: 100% passing across 9 test suites (`DollEvolutionTest`, `DollCatalogTest`, `RewardDaoFakeTest`, `TaskRepositoryRewardTest`, `CelebrationDialogLogicTest`, `TodayViewModelRewardTest`, `HistoryRewardShelfTest`, `HistorySummaryTest`, `ThemeColorContrastTest`).
- Hardware Verification: Successfully installed and visually verified on TECNO POVA 5 via ADB & Mobile-MCP.
