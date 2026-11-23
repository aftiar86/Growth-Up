# Mega Doll Collection & Star Evolution System Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Expand the daily reward system to 30 unique cute dolls grouped into 5 thematic series with a 3-star accessory evolution system (⭐ Level 1, ⭐⭐ Level 2 with Ribbon 🎀, ⭐⭐⭐ Level 3 with Queen Crown 👑).

**Architecture:** Domain catalog provides 30 doll rewards divided across 5 `DollSeries`. A pure calculation utility `DollEvolutionCalculator` derives a doll's star tier and accessory level from historical unlocks in `unlocked_rewards`. `CelebrationDialog` showcases the star level and upgrade notification, while `HistoryScreen` features interactive series tab filtering and star badges.

**Tech Stack:** Kotlin 2.0.21, Jetpack Compose Material 3, Room SQLite Database, Coroutines/StateFlow, ProGuard/R8 minification (keeping APK ~1.2 MB).

**Spec:** `docs/superpowers/specs/2026-11-23-mega-doll-collection-system.md`

## Global Constraints

- **APK Size Limit:** Maintain minimal size (~1.2 MB); zero external graphic/animation libraries; all badges, stars, and accessories are rendered with native Compose text/vectors.
- **Backwards Compatibility:** Existing `unlocked_rewards` table and records are preserved without wiping user history.
- **Theme Consistency:** Soft pink minimal aesthetics (`GrowthUpColors.WarmRose`, `GrowthUpColors.SoftBlush`, `GrowthUpColors.SurfaceWhite`, `GrowthUpColors.BackgroundCream`, `GrowthUpColors.TextCharcoal`).
- **TDD:** Write failing test first, verify failure, implement minimal code, verify pass, commit.

## Review Focus

1. **Deterministic 30-day rotation:** Across 30 consecutive calendar days, 30 distinct dolls are selected before repeating.
2. **Evolution calculation accuracy:** 0 unlocks = locked (0 stars); 1 unlock = 1 star; 2 unlocks = 2 stars (ribbon accessory); 3+ unlocks = 3 stars (crown accessory).
3. **Series filtering:** Selecting a series tab in the History shelf accurately filters the 6 dolls belonging to that series.
4. **Overall collection counter:** Correctly tallies distinct unlocked dolls out of 30 (e.g., "1/30 Terkumpul").
5. **Level up celebration notice:** When a doll is unlocked for the 2nd or 3rd time, the celebration dialog displays an affectionate level-up announcement.

---

### Task 1: Domain Model (30 Dolls, 5 Series & Evolution Calculator)

**Files:**
- Modify: `app/src/main/java/com/growthup/app/domain/model/DollReward.kt`
- Modify: `app/src/main/java/com/growthup/app/domain/model/DollCatalog.kt`
- Create: `app/src/main/java/com/growthup/app/domain/model/DollEvolution.kt`
- Modify: `app/src/test/java/com/growthup/app/DollCatalogTest.kt`
- Create: `app/src/test/java/com/growthup/app/DollEvolutionTest.kt`

**Interfaces:**
- Produces:
  ```kotlin
  data class DollSeries(val id: String, val title: String, val icon: String)
  data class DollEvolution(val dollId: String, val unlockCount: Int, val starLevel: Int, val accessoryEmoji: String?, val titleBadge: String)
  object DollEvolutionCalculator {
      fun calculateEvolution(dollId: String, totalUnlocks: Int): DollEvolution
  }
  ```

- [ ] **Step 1: Write failing unit test for 30 dolls and evolution calculator**

Create `app/src/test/java/com/growthup/app/DollEvolutionTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import com.growthup.app.domain.model.DollEvolutionCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DollEvolutionTest {

    @Test
    fun testCatalogHasThirtyDistinctDollsInFiveSeries() {
        val dolls = DollCatalog.ALL_DOLLS
        assertEquals(30, dolls.size)
        assertEquals(30, dolls.map { it.id }.toSet().size)
        assertEquals(5, DollCatalog.SERIES_LIST.size)
    }

    @Test
    fun testEvolutionCalculatorTiers() {
        val level0 = DollEvolutionCalculator.calculateEvolution("kapibara", 0)
        assertEquals(0, level0.starLevel)
        assertNull(level0.accessoryEmoji)

        val level1 = DollEvolutionCalculator.calculateEvolution("kapibara", 1)
        assertEquals(1, level1.starLevel)
        assertNull(level1.accessoryEmoji)
        assertEquals("⭐ Sahabat Baru", level1.titleBadge)

        val level2 = DollEvolutionCalculator.calculateEvolution("kapibara", 2)
        assertEquals(2, level2.starLevel)
        assertEquals("🎀", level2.accessoryEmoji)
        assertEquals("⭐⭐ Sahabat Setia", level2.titleBadge)

        val level3 = DollEvolutionCalculator.calculateEvolution("kapibara", 5)
        assertEquals(3, level3.starLevel)
        assertEquals("👑", level3.accessoryEmoji)
        assertEquals("⭐⭐⭐ Boneka Ratu", level3.titleBadge)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.DollEvolutionTest`
Expected: FAIL.

- [ ] **Step 3: Implement DollSeries, 30 Dolls in DollCatalog, and DollEvolutionCalculator**

Update `DollReward.kt`: Add `val seriesId: String = "seri_1"`.
Create `DollEvolution.kt` with `DollEvolution` and `DollEvolutionCalculator`.
Update `DollCatalog.kt`: Populate 30 distinct dolls divided into 5 series (6 dolls per series).
Update `getRewardForDate` to cycle through 30 dolls (`dayOfYear % 30`).

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.DollEvolutionTest`
Expected: PASS.

- [ ] **Step 5: Run existing DollCatalogTest**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.DollCatalogTest`
Expected: PASS.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/growthup/app/domain/model/ app/src/test/java/com/growthup/app/
git commit -m "feat(catalog): add 30 unique dolls across 5 series and DollEvolutionCalculator"
```

---

### Task 2: CelebrationDialog Star Level & Upgrade Display

**Files:**
- Modify: `app/src/main/java/com/growthup/app/ui/components/CelebrationDialog.kt`
- Test: `app/src/test/java/com/growthup/app/CelebrationDialogLogicTest.kt`

**Interfaces:**
- Consumes: `DollReward`, `DollEvolution`
- Produces:
  ```kotlin
  @Composable
  fun CelebrationDialog(
      reward: DollReward,
      evolution: DollEvolution? = null,
      onDismiss: () -> Unit
  )
  ```

- [ ] **Step 1: Write test for evolution badge formatting**

Update `CelebrationDialogLogicTest.kt` to assert all 30 dolls have non-blank messages and valid series.

- [ ] **Step 2: Implement star badge and accessory display in CelebrationDialog**

In `CelebrationDialog.kt`:
- Add `evolution: DollEvolution? = null` parameter.
- If `evolution != null && evolution.starLevel > 1`:
  - Show cute accessory emoji (e.g. `🎀` or `👑`) overlay on the doll avatar.
  - Show star rating pill: e.g. "⭐⭐ Sahabat Setia (Naik Level!)" or "⭐⭐⭐ Boneka Ratu!".
- If `evolution?.starLevel == 1`:
  - Show "⭐ Sahabat Baru Tersimpan!".

- [ ] **Step 3: Verify compilation**

Run: `./gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/components/CelebrationDialog.kt app/src/test/java/com/growthup/app/CelebrationDialogLogicTest.kt
git commit -m "feat(ui): display star level and accessory badge in CelebrationDialog"
```

---

### Task 3: TodayViewModel Evolution Integration

**Files:**
- Modify: `app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt`
- Modify: `app/src/main/java/com/growthup/app/ui/today/TodayScreen.kt`
- Test: `app/src/test/java/com/growthup/app/TodayViewModelRewardTest.kt`

**Interfaces:**
- Produces:
  ```kotlin
  data class TodayUiState(
      ...
      val celebrationReward: DollReward? = null,
      val celebrationEvolution: DollEvolution? = null,
      val showCelebrationDialog: Boolean = false
  )
  ```

- [ ] **Step 1: Update TodayViewModelRewardTest to verify celebrationEvolution state**

Add assertion that `celebrationEvolution` is populated when celebration triggers.

- [ ] **Step 2: Run test to verify failure**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TodayViewModelRewardTest`
Expected: FAIL.

- [ ] **Step 3: Implement calculation in TodayViewModel**

When unlocking reward, calculate unlock count for this `doll.id` across all rewards and store `celebrationEvolution = DollEvolutionCalculator.calculateEvolution(doll.id, totalCount)`.
Pass `evolution = state.celebrationEvolution` to `CelebrationDialog` in `TodayScreen.kt`.

- [ ] **Step 4: Run test to verify pass**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TodayViewModelRewardTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt app/src/main/java/com/growthup/app/ui/today/TodayScreen.kt app/src/test/java/com/growthup/app/TodayViewModelRewardTest.kt
git commit -m "feat(today): calculate and expose doll evolution during daily celebration"
```

---

### Task 4: HistoryScreen Series Selector & Star Badges

**Files:**
- Modify: `app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt`
- Modify: `app/src/main/java/com/growthup/app/ui/history/HistoryScreen.kt`
- Test: `app/src/test/java/com/growthup/app/HistoryRewardShelfTest.kt`

**Interfaces:**
- Produces:
  - Series filter tabs in HistoryScreen
  - Star ratings ⭐/⭐⭐/⭐⭐⭐ on each doll card
  - Total unlocked counter `${unlockedCount}/30 Koleksi`

- [ ] **Step 1: Add unit test in HistoryRewardShelfTest for series filtering & evolution**

Test that doll unlocks accurately reflect counts and series groupings.

- [ ] **Step 2: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.HistoryRewardShelfTest`
Expected: PASS.

- [ ] **Step 3: Update HistoryScreen with Series Tabs and Star Badges**

In `HistoryScreen.kt`:
- Add a Series selector pill row (`LazyRow`) for the 5 series:
  - "Semua", "🌲 Hutan", "🎀 Manis", "🌊 Laut", "🌸 Kebun", "✨ Fantasi".
- Calculate evolution for each doll based on `state.unlockedRewards`.
- For unlocked dolls, render:
  - Doll emoji with accessory badge (`🎀` or `👑`)
  - Star rating text (`⭐`, `⭐⭐`, or `⭐⭐⭐`)
  - Unlock count tag (`x${count}`)
- Summary counter: `${unlockedDollsCount}/30 Boneka Terkumpul 💕`.

- [ ] **Step 4: Run all unit tests**

Run: `./gradlew.bat testDebugUnitTest`
Expected: All unit tests PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/history/HistoryScreen.kt app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt app/src/test/java/com/growthup/app/HistoryRewardShelfTest.kt
git commit -m "feat(history): add series tabs and star level badges to Rak Koleksi Boneka"
```

---

### Task 5: Verification, Release Build, Device Install, Mobile-MCP & Git Push

- [ ] **Step 1: Run full unit test suite**
Run: `./gradlew.bat testDebugUnitTest`
Expected: 100% PASS.

- [ ] **Step 2: Build release APK and verify size**
Run: `./gradlew.bat assembleRelease`
Expected: BUILD SUCCESSFUL, APK size ~1.2 MB.

- [ ] **Step 3: Install on TECNO POVA 5 via ADB**
Run: `adb -s 106293738V007469 install -r "app\build\outputs\apk\release\app-release.apk"`
Expected: Success.

- [ ] **Step 4: Launch and verify visually via Mobile-MCP**
Verify Today celebration, History series tabs, star ratings, and take screenshots.

- [ ] **Step 5: Update Graphify report and push to GitHub**
Push all commits to `https://github.com/aftiar86/Growth-Up`.
