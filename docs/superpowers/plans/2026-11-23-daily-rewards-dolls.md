# Daily Completion Rewards & Collectible Doll Shelf Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Provide an affectionate celebration pop-up with unique collectible cute dolls (Kapibara, Hello Kitty, Bunny, Teddy Bear, Panda, Shiba Inu) when 100% of today's tasks are completed, and showcase unlocked dolls in a collection shelf on the History tab.

**Architecture:** A domain catalog defines 6 lovable doll characters and a deterministic daily selector. A Room database table (`unlocked_rewards`) persists unlocks across app launches. The `TodayViewModel` monitors task completion to trigger the animated `CelebrationDialog` once per day, while `HistoryViewModel` exposes unlocked rewards to display on a dedicated "Rak Koleksi Boneka" shelf in `HistoryScreen`.

**Tech Stack:** Kotlin 2.0.21, Jetpack Compose Material 3, Room SQLite, StateFlow / Coroutines, ProGuard/R8 minification (keeping APK ~1.2 MB).

**Spec:** `docs/superpowers/specs/2026-11-23-daily-rewards-dolls-design.md`

## Global Constraints

- **APK Size Limit:** Maintain minimal footprint (~1.2 MB); do not introduce heavy animation or 3D libraries (Lottie, Filament, Sceneview); use native Compose animations (`Animatable`, `spring`, `Canvas`).
- **Design Tokens:** Warm Rose (`0xFFE07A5F`), Soft Blush (`0xFFFFD6D6`), Cream Background (`0xFFFDF8F5`), Text Charcoal (`0xFF2B2D42`).
- **Tone:** Sweet, caring, affectionate, and cheerful Indonesian copy tailored for the user's girlfriend.
- **TDD:** Write failing test first, verify failure, implement minimal code, verify pass, commit.

## Review Focus

1. **Zero tasks today (0/0 tasks):** Empty task list must not evaluate to 100% or trigger the celebration dialog.
2. **Repeated toggles on same day:** Unchecking and re-checking a task after already receiving the reward must not spam or re-open the dialog.
3. **Database schema upgrade (v1 to v2):** Existing tasks in `tasks` table must survive without crashes or data wipe using fallback destructive or structured migration.
4. **History shelf with zero unlocks:** When no rewards have been unlocked yet, the shelf must render mystery/silhouette placeholders cleanly without index-out-of-bounds errors.
5. **Deterministic doll rotation:** Rewards across different calendar dates cycle through the 6 catalog characters so she gets a distinct cute doll every day.

---

### Task 1: Domain Model & Doll Catalog

**Files:**
- Create: `app/src/main/java/com/growthup/app/domain/model/DollReward.kt`
- Create: `app/src/main/java/com/growthup/app/domain/model/DollCatalog.kt`
- Test: `app/src/test/java/com/growthup/app/DollCatalogTest.kt`

**Interfaces:**
- Produces:
  ```kotlin
  data class DollReward(
      val id: String,
      val name: String,
      val subtitle: String,
      val emoji: String,
      val accentColorHex: Long,
      val praiseMessage: String
  )
  object DollCatalog {
      val ALL_DOLLS: List<DollReward>
      fun getDollById(id: String): DollReward?
      fun getRewardForDate(dateString: String): DollReward
  }
  ```

- [ ] **Step 1: Write failing unit test for DollCatalog**

Create `app/src/test/java/com/growthup/app/DollCatalogTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DollCatalogTest {

    @Test
    fun testCatalogHasSixDistinctDolls() {
        val dolls = DollCatalog.ALL_DOLLS
        assertEquals(6, dolls.size)
        val ids = dolls.map { it.id }.toSet()
        assertEquals(6, ids.size)
    }

    @Test
    fun testCatalogContainsKapibaraAndHelloKitty() {
        val kapibara = DollCatalog.getDollById("kapibara")
        assertNotNull(kapibara)
        assertTrue(kapibara!!.name.contains("Kapibara"))

        val kitty = DollCatalog.getDollById("hello_kitty")
        assertNotNull(kitty)
        assertTrue(kitty!!.name.contains("Hello Kitty"))
    }

    @Test
    fun testGetRewardForDateIsDeterministicAndRotates() {
        val reward1 = DollCatalog.getRewardForDate("2026-11-23")
        val reward2 = DollCatalog.getRewardForDate("2026-11-23")
        assertEquals(reward1.id, reward2.id)

        val rewardNextDay = DollCatalog.getRewardForDate("2026-11-24")
        assertNotNull(rewardNextDay)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.DollCatalogTest`
Expected: FAIL (unresolved reference: DollCatalog).

- [ ] **Step 3: Implement DollReward and DollCatalog**

Create `app/src/main/java/com/growthup/app/domain/model/DollReward.kt`:
```kotlin
package com.growthup.app.domain.model

data class DollReward(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val accentColorHex: Long,
    val praiseMessage: String
)
```

Create `app/src/main/java/com/growthup/app/domain/model/DollCatalog.kt`:
```kotlin
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
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.DollCatalogTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/domain/model/DollReward.kt app/src/main/java/com/growthup/app/domain/model/DollCatalog.kt app/src/test/java/com/growthup/app/DollCatalogTest.kt
git commit -m "feat(rewards): add DollReward domain model and DollCatalog with unit tests"
```

---

### Task 2: Room Entity, DAO, and Database Upgrade

**Files:**
- Create: `app/src/main/java/com/growthup/app/data/model/UnlockedRewardEntity.kt`
- Create: `app/src/main/java/com/growthup/app/data/local/RewardDao.kt`
- Modify: `app/src/main/java/com/growthup/app/data/local/GrowthUpDatabase.kt`
- Test: `app/src/test/java/com/growthup/app/RewardDaoFakeTest.kt`

**Interfaces:**
- Consumes: `GrowthUpDatabase`
- Produces:
  ```kotlin
  @Entity(tableName = "unlocked_rewards")
  data class UnlockedRewardEntity(
      @PrimaryKey val date: String,
      val dollId: String,
      val unlockedAt: Long = System.currentTimeMillis()
  )
  @Dao
  interface RewardDao {
      @Query("SELECT * FROM unlocked_rewards ORDER BY date DESC")
      fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>>

      @Query("SELECT * FROM unlocked_rewards WHERE date = :date LIMIT 1")
      suspend fun getRewardForDate(date: String): UnlockedRewardEntity?

      @Insert(onConflict = OnConflictStrategy.IGNORE)
      suspend fun insertReward(reward: UnlockedRewardEntity): Long
  }
  ```

- [ ] **Step 1: Write unit test for Reward Entity & DAO Contract**

Create `app/src/test/java/com/growthup/app/RewardDaoFakeTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.data.local.RewardDao
import com.growthup.app.data.model.UnlockedRewardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FakeRewardDao : RewardDao {
    private val rewards = MutableStateFlow<Map<String, UnlockedRewardEntity>>(emptyMap())

    override fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>> {
        return kotlinx.coroutines.flow.map(rewards) { it.values.sortedByDescending { r -> r.date } }
    }

    override suspend fun getRewardForDate(date: String): UnlockedRewardEntity? {
        return rewards.value[date]
    }

    override suspend fun insertReward(reward: UnlockedRewardEntity): Long {
        if (rewards.value.containsKey(reward.date)) return -1L
        rewards.value = rewards.value + (reward.date to reward)
        return 1L
    }
}

class RewardDaoFakeTest {
    @Test
    fun testInsertAndRetrieveReward() = runBlocking {
        val dao = FakeRewardDao()
        assertNull(dao.getRewardForDate("2026-11-23"))

        val entity = UnlockedRewardEntity(date = "2026-11-23", dollId = "kapibara")
        val insertResult = dao.insertReward(entity)
        assertEquals(1L, insertResult)

        val retrieved = dao.getRewardForDate("2026-11-23")
        assertNotNull(retrieved)
        assertEquals("kapibara", retrieved?.dollId)

        // Ignore duplicate insert on same date
        val duplicateInsert = dao.insertReward(entity)
        assertEquals(-1L, duplicateInsert)

        val list = dao.getAllUnlockedRewards().first()
        assertEquals(1, list.size)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.RewardDaoFakeTest`
Expected: FAIL (unresolved reference: UnlockedRewardEntity, RewardDao).

- [ ] **Step 3: Implement UnlockedRewardEntity, RewardDao, and Update GrowthUpDatabase**

Create `app/src/main/java/com/growthup/app/data/model/UnlockedRewardEntity.kt`:
```kotlin
package com.growthup.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlocked_rewards")
data class UnlockedRewardEntity(
    @PrimaryKey
    val date: String,
    val dollId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)
```

Create `app/src/main/java/com/growthup/app/data/local/RewardDao.kt`:
```kotlin
package com.growthup.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.growthup.app.data.model.UnlockedRewardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM unlocked_rewards ORDER BY date DESC")
    fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>>

    @Query("SELECT * FROM unlocked_rewards WHERE date = :date LIMIT 1")
    suspend fun getRewardForDate(date: String): UnlockedRewardEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReward(reward: UnlockedRewardEntity): Long
}
```

Update `app/src/main/java/com/growthup/app/data/local/GrowthUpDatabase.kt`:
Bump version to 2 and add `UnlockedRewardEntity` to entities array:
```kotlin
@Database(entities = [TaskEntity::class, UnlockedRewardEntity::class], version = 2, exportSchema = false)
abstract class GrowthUpDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun rewardDao(): RewardDao
    ...
```

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.RewardDaoFakeTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/data/model/UnlockedRewardEntity.kt app/src/main/java/com/growthup/app/data/local/RewardDao.kt app/src/main/java/com/growthup/app/data/local/GrowthUpDatabase.kt app/src/test/java/com/growthup/app/RewardDaoFakeTest.kt
git commit -m "feat(database): add UnlockedRewardEntity and RewardDao with database version 2 migration"
```

---

### Task 3: Repository Integration for Rewards

**Files:**
- Modify: `app/src/main/java/com/growthup/app/domain/repository/TaskRepository.kt`
- Modify: `app/src/main/java/com/growthup/app/data/repository/TaskRepositoryImpl.kt`
- Modify: `app/src/main/java/com/growthup/app/MainActivity.kt`
- Test: `app/src/test/java/com/growthup/app/TaskRepositoryRewardTest.kt`

**Interfaces:**
- Consumes: `RewardDao`, `TaskDao`
- Produces:
  ```kotlin
  interface TaskRepository {
      ...
      fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>>
      suspend fun getRewardForDate(date: String): UnlockedRewardEntity?
      suspend fun unlockRewardForDate(date: String, dollId: String): Boolean
  }
  ```

- [ ] **Step 1: Write failing test in TaskRepositoryRewardTest**

Create `app/src/test/java/com/growthup/app/TaskRepositoryRewardTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.data.model.UnlockedRewardEntity
import com.growthup.app.data.repository.TaskRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskRepositoryRewardTest {

    @Test
    fun testUnlockRewardAndRetrieve() = runBlocking {
        val fakeTaskDao = FakeReactiveTaskDao()
        val fakeRewardDao = FakeRewardDao()
        val repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)

        val date = "2026-11-23"
        val unlocked = repository.unlockRewardForDate(date, "kapibara")
        assertTrue(unlocked)

        val retrieved = repository.getRewardForDate(date)
        assertNotNull(retrieved)
        assertEquals("kapibara", retrieved?.dollId)

        // Attempting to unlock again on the same day should return false (already unlocked)
        val secondAttempt = repository.unlockRewardForDate(date, "hello_kitty")
        assertFalse(secondAttempt)

        val allRewards = repository.getAllUnlockedRewards().first()
        assertEquals(1, allRewards.size)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TaskRepositoryRewardTest`
Expected: FAIL (TaskRepositoryImpl does not have fakeRewardDao parameter or methods).

- [ ] **Step 3: Implement reward methods in TaskRepository and TaskRepositoryImpl**

Update `app/src/main/java/com/growthup/app/domain/repository/TaskRepository.kt`:
```kotlin
fun getAllUnlockedRewards(): Flow<List<com.growthup.app.data.model.UnlockedRewardEntity>>
suspend fun getRewardForDate(date: String): com.growthup.app.data.model.UnlockedRewardEntity?
suspend fun unlockRewardForDate(date: String, dollId: String): Boolean
```

Update `app/src/main/java/com/growthup/app/data/repository/TaskRepositoryImpl.kt`:
Add `rewardDao: RewardDao? = null` parameter (default null for backwards compatibility with tests).
Implement:
```kotlin
override fun getAllUnlockedRewards(): Flow<List<UnlockedRewardEntity>> {
    return rewardDao?.getAllUnlockedRewards() ?: kotlinx.coroutines.flow.flowOf(emptyList())
}

override suspend fun getRewardForDate(date: String): UnlockedRewardEntity? {
    return rewardDao?.getRewardForDate(date)
}

override suspend fun unlockRewardForDate(date: String, dollId: String): Boolean {
    val existing = rewardDao?.getRewardForDate(date)
    if (existing != null) return false
    val result = rewardDao?.insertReward(UnlockedRewardEntity(date = date, dollId = dollId))
    return (result != null && result > 0L)
}
```

Update `app/src/main/java/com/growthup/app/MainActivity.kt`:
Pass `database.rewardDao()` to `TaskRepositoryImpl(database.taskDao(), database.rewardDao())`.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TaskRepositoryRewardTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/domain/repository/TaskRepository.kt app/src/main/java/com/growthup/app/data/repository/TaskRepositoryImpl.kt app/src/main/java/com/growthup/app/MainActivity.kt app/src/test/java/com/growthup/app/TaskRepositoryRewardTest.kt
git commit -m "feat(repository): implement reward unlocking and querying in TaskRepository"
```

---

### Task 4: Celebration Dialog Component (Compose UI / UX Promax)

**Files:**
- Create: `app/src/main/java/com/growthup/app/ui/components/CelebrationDialog.kt`
- Test: `app/src/test/java/com/growthup/app/CelebrationDialogLogicTest.kt`

**Interfaces:**
- Consumes: `DollReward`
- Produces:
  ```kotlin
  @Composable
  fun CelebrationDialog(
      reward: DollReward,
      onDismiss: () -> Unit
  )
  ```

- [ ] **Step 1: Write test for Celebration copy and state formatting**

Create `app/src/test/java/com/growthup/app/CelebrationDialogLogicTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.domain.model.DollCatalog
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CelebrationDialogLogicTest {

    @Test
    fun testPraiseMessageHasAffectionateTone() {
        DollCatalog.ALL_DOLLS.forEach { doll ->
            assertFalse(doll.praiseMessage.isBlank())
            assertTrue(doll.emoji.isNotBlank())
            assertTrue(doll.subtitle.isNotBlank())
        }
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.CelebrationDialogLogicTest`
Expected: PASS.

- [ ] **Step 3: Create CelebrationDialog.kt with smooth Compose animation and soft pink styling**

Create `app/src/main/java/com/growthup/app/ui/components/CelebrationDialog.kt`:
- Use `androidx.compose.ui.window.Dialog`
- Animate character bounce with `rememberInfiniteTransition` using gentle sine wave oscillation for scale & offset.
- Add confetti sparkle particles rendered with lightweight Canvas dots (blush pink, soft gold, warm rose) to keep APK ultra-light (~1.2 MB, no external library).
- Display:
  - Big circular avatar with `reward.emoji` (e.g., 68.sp)
  - Badge with `reward.name` and `reward.subtitle`
  - Heart icon & "100% Selesai! Kamu Hebat! 💖"
  - `reward.praiseMessage` in sweet readable typography
  - Cute rounded dismiss button: "Simpan ke Koleksiku 🎀"
- Soft rounded corners (28.dp), cream surface, warm rose primary colors.

- [ ] **Step 4: Verify full project compilation**

Run: `./gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/components/CelebrationDialog.kt app/src/test/java/com/growthup/app/CelebrationDialogLogicTest.kt
git commit -m "feat(ui): create CelebrationDialog with animated character bounce and confetti"
```

---

### Task 5: Today Screen & ViewModel Integration

**Files:**
- Modify: `app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt`
- Modify: `app/src/main/java/com/growthup/app/ui/today/TodayScreen.kt`
- Test: `app/src/test/java/com/growthup/app/TodayViewModelRewardTest.kt`

**Interfaces:**
- Consumes: `TaskRepository`, `DollCatalog`, `CelebrationDialog`
- Produces:
  ```kotlin
  data class TodayUiState(
      ...
      val celebrationReward: DollReward? = null,
      val showCelebrationDialog: Boolean = false
  )
  class TodayViewModel {
      fun dismissCelebrationDialog()
  }
  ```

- [ ] **Step 1: Write unit tests for celebration trigger in TodayViewModel**

Create `app/src/test/java/com/growthup/app/TodayViewModelRewardTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.ui.today.TodayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelRewardTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeTaskDao: FakeReactiveTaskDao
    private lateinit var fakeRewardDao: FakeRewardDao
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var viewModel: TodayViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskDao = FakeReactiveTaskDao()
        fakeRewardDao = FakeRewardDao()
        repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)
        viewModel = TodayViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testZeroTasksDoesNotTriggerCelebration() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(0, state.totalCount)
        assertFalse(state.showCelebrationDialog)
        assertNull(state.celebrationReward)
    }

    @Test
    fun testCompletingAllTasksTriggersCelebration() = runTest {
        val today = LocalDate.now().toString()
        viewModel.addTaskForToday("Baca Buku 30 Menit")
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertEquals(1, state.totalCount)
        assertEquals(0, state.completedCount)
        assertFalse(state.showCelebrationDialog)

        // Complete the task
        val taskId = state.tasks.first().id
        viewModel.toggleTask(taskId, true)
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.completedCount)
        assertEquals(100, state.progressPercent)
        assertTrue(state.showCelebrationDialog)
        assertNotNull(state.celebrationReward)

        // Dismiss dialog
        viewModel.dismissCelebrationDialog()
        assertFalse(viewModel.uiState.value.showCelebrationDialog)

        // Toggling back and forth on the same day should not trigger duplicate celebration
        viewModel.toggleTask(taskId, false)
        advanceUntilIdle()
        viewModel.toggleTask(taskId, true)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showCelebrationDialog)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TodayViewModelRewardTest`
Expected: FAIL (unresolved reference: celebrationReward, dismissCelebrationDialog).

- [ ] **Step 3: Implement celebration trigger logic in TodayViewModel and TodayScreen**

In `app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt`:
1. Add `celebrationReward: DollReward? = null` and `showCelebrationDialog: Boolean = false` to `TodayUiState`.
2. Add a private boolean `var hasShownCelebrationToday: Boolean = false`.
3. In `loadTasks()`, when `total > 0 && completed == total`:
   - Check `repository.getRewardForDate(today)`.
   - If null and `!hasShownCelebrationToday`:
     - Select doll with `DollCatalog.getRewardForDate(today)`.
     - Call `repository.unlockRewardForDate(today, doll.id)`.
     - Update state with `celebrationReward = doll` and `showCelebrationDialog = true`.
     - Mark `hasShownCelebrationToday = true`.
4. Add `fun dismissCelebrationDialog()` which updates `showCelebrationDialog = false`.

In `app/src/main/java/com/growthup/app/ui/today/TodayScreen.kt`:
If `state.showCelebrationDialog && state.celebrationReward != null`:
Render `CelebrationDialog(reward = state.celebrationReward!!, onDismiss = viewModel::dismissCelebrationDialog)`.

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.TodayViewModelRewardTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt app/src/main/java/com/growthup/app/ui/today/TodayScreen.kt app/src/test/java/com/growthup/app/TodayViewModelRewardTest.kt
git commit -m "feat(today): integrate completion celebration dialog with TodayViewModel and TodayScreen"
```

---

### Task 6: History Screen "Rak Koleksi Boneka" (Doll Collection Shelf)

**Files:**
- Modify: `app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt`
- Modify: `app/src/main/java/com/growthup/app/ui/history/HistoryScreen.kt`
- Test: `app/src/test/java/com/growthup/app/HistoryRewardShelfTest.kt`

**Interfaces:**
- Consumes: `DollCatalog`, `UnlockedRewardEntity`, `TaskRepository`
- Produces:
  ```kotlin
  data class HistoryUiState(
      ...
      val unlockedRewards: List<UnlockedRewardEntity> = emptyList()
  )
  ```

- [ ] **Step 1: Write test for HistoryViewModel reward integration**

Create `app/src/test/java/com/growthup/app/HistoryRewardShelfTest.kt`:
```kotlin
package com.growthup.app

import com.growthup.app.data.model.UnlockedRewardEntity
import com.growthup.app.data.repository.TaskRepositoryImpl
import com.growthup.app.ui.history.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryRewardShelfTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeTaskDao: FakeReactiveTaskDao
    private lateinit var fakeRewardDao: FakeRewardDao
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskDao = FakeReactiveTaskDao()
        fakeRewardDao = FakeRewardDao()
        repository = TaskRepositoryImpl(fakeTaskDao, fakeRewardDao)
        viewModel = HistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testHistoryObservesUnlockedRewards() = runTest {
        advanceUntilIdle()
        assertEquals(0, viewModel.uiState.value.unlockedRewards.size)

        fakeRewardDao.insertReward(UnlockedRewardEntity("2026-11-23", "kapibara"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.unlockedRewards.size)
        assertEquals("kapibara", state.unlockedRewards.first().dollId)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.HistoryRewardShelfTest`
Expected: FAIL (unresolved reference: unlockedRewards).

- [ ] **Step 3: Update HistoryViewModel and HistoryScreen with Doll Shelf**

In `app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt`:
1. Add `val unlockedRewards: List<UnlockedRewardEntity> = emptyList()` to `HistoryUiState`.
2. In `loadHistory()`, launch a collector for `repository.getAllUnlockedRewards()` updating `_uiState.value.copy(unlockedRewards = rewards)`.

In `app/src/main/java/com/growthup/app/ui/history/HistoryScreen.kt`:
Add "Rak Koleksi Boneka 🎀":
- Horizontal scroll row or 2-column grid showing the 6 dolls from `DollCatalog.ALL_DOLLS`.
- If a doll's ID is present in `state.unlockedRewards`:
  - Show full color badge, doll emoji, doll name, and unlock date tag ("Terbuka ✨").
- If locked:
  - Show mystery card with translucent silhouette, lock icon `🔒`, and subtitle "Selesaikan 100% harimu".
- Add summary badge: "${unlockedCount}/6 Boneka Terkumpul 💕".

- [ ] **Step 4: Run test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.growthup.app.HistoryRewardShelfTest`
Expected: PASS.

- [ ] **Step 5: Run all unit tests**

Run: `./gradlew.bat testDebugUnitTest`
Expected: All unit tests PASS.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt app/src/main/java/com/growthup/app/ui/history/HistoryScreen.kt app/src/test/java/com/growthup/app/HistoryRewardShelfTest.kt
git commit -m "feat(history): add Rak Koleksi Boneka showcasing unlocked cute doll rewards"
```

---

### Task 7: Verification, Release APK Build, Device Install & Push

**Files:**
- Modify: `graphify-out/` (Update knowledge graph via Graphify)

**Steps:**
- [ ] **Step 1: Run complete test suite**

Run: `./gradlew.bat testDebugUnitTest`
Expected: ALL PASS.

- [ ] **Step 2: Build release APK and verify size**

Run: `./gradlew.bat assembleRelease`
Expected: BUILD SUCCESSFUL, output file at `app/build/outputs/apk/release/app-release.apk`.
Verify file size is ~1.2 MB (well under 5 MB limit).

- [ ] **Step 3: Install APK on TECNO POVA 5 hardware device**

Run: `adb -s 106293738V007469 install -r "app\build\outputs\apk\release\app-release.apk"`
Expected: Success.

- [ ] **Step 4: Launch and verify UI on device using Mobile-MCP**

Run ADB command to launch `com.growthup.app/.MainActivity`:
`adb -s 106293738V007469 shell am start -n com.growthup.app/.MainActivity`
Capture screenshot to `C:\Users\AFTIAR\AppData\Local\Temp\growth_up_reward_verified.png` using Mobile-MCP `mobile_take_screenshot`.

- [ ] **Step 5: Update Graphify Knowledge Graph**

Run python script to regenerate Graphify graph:
`python .gemini/skills/graphify/graphify.py . --output graphify-out` (or project graph script).

- [ ] **Step 6: Git push to remote repository**

Push all commits to `https://github.com/aftiar86/Growth-Up`:
```bash
git push origin main
```
