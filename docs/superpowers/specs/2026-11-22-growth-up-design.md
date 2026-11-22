# Design Document: Growth Up Daily Planner

**Date:** 2026-11-22
**Project Name:** Growth Up
**Target Platform:** Android (Kotlin, Jetpack Compose)

---

## 1. Intent and The Why

The user often plans activities for tomorrow in their mind, but frequently forgets them during the day and fails to finish them. They need a companion app that creates a clear daily rhythm:
1. **The evening before (H-1):** Deliberately write down and commit to tomorrow's list of tasks.
2. **The day of execution (Day H):** Keep the planned tasks front-and-center, check them off as they are completed, and receive friendly reminders.
3. **End of day / Next cycle:** Evaluate what was done versus what wasn't. Unfinished tasks can either be rolled over to tomorrow with one tap or archived/ignored for self-evaluation without guilt.

The target audience is women, desiring a clean, minimalist, beautiful UI with warm, soft pink aesthetic accents (*blush / soft pink / rose*).

---

## 2. Goals, Non-Goals, and Anti-Goals

### Goals
- **Two-phase daily rhythm:** Dedicated view for active Day H checklist and separate preparation view for H-1 planning.
- **Low-friction task creation:** Fast, effortless addition of activities during evening planning.
- **Compassionate rollover & evaluation:** Unfinished tasks at the end of Day H can be easily moved to the next day's plan or dismissed/ignored.
- **Timely notifications:** Evening notification reminding user to plan tomorrow; daytime reminder to check off remaining tasks.
- **Minimalist & aesthetic UI:** Soft pink theme, clean typography, 48dp touch targets, smooth check animations, no clutter.
- **Offline-first reliability:** Stored locally in SQLite (via Room) so it works instantly without internet.
- **Direct deployment & testing:** Tested on connected physical Android device via USB using Mobile-MCP and release APK installation.

### Non-Goals
- Complex project management features (no Gantt charts, sub-projects, nested folders, team sharing).
- Cloud sync / account login in v1 (pure offline local storage to keep it fast, private, and simple).
- Gamified punishment or guilt-inducing streak breaks.

### Anti-Goals
- **Endless backlog syndrome:** Do not let tasks pile up into an intimidating infinite backlog; tasks belong to specific days.
- **Over-engineered UI:** No visual clutter, excessive buttons, or distracting animations.

---

## 3. Constraints & Tech Stack

- **Platform:** Android (Min SDK 26, Target SDK 34/35).
- **Language:** Kotlin.
- **UI Framework:** Jetpack Compose with Material 3 theming (custom Soft Pink aesthetic tokens).
- **Architecture:** Single-activity, Clean MVVM architecture (UI Layer -> ViewModel -> Repository -> Room DAO).
- **Local Database:** Room Database (SQLite) + Kotlin Coroutines & Flow.
- **Notifications:** Android `NotificationManager` + `AlarmManager` or `WorkManager` for scheduled evening and day reminders.
- **Repository & Version Control:** Git repository "Growth Up", frequent commits, pushed to user's GitHub.
- **Tooling & Skills integration:**
  - `ui-ux-pro-max`: Design tokens, contrast >=4.5:1, 48dp touch targets, soft pink palette.
  - `ponytail`: Minimalist, zero-bloat code (YAGNI, DRY, idiomatic Kotlin).
  - `graphify`: Knowledge graph of codebase structure for efficient navigation.
  - `mobile-mcp`: Testing and launching on the USB-connected Android device.

---

## 4. Key Decisions & User Choices

1. **Planning Schedule:**
   - Tasks are entered for "Tomorrow" (H-1).
   - Once midnight passes (or when the user opens the app on Day H), tomorrow's plan becomes today's active checklist.
2. **Handling Unfinished Tasks:**
   - Unfinished tasks from today can either be:
     - Moved to tomorrow's plan (`Rollover`), or
     - Marked as archived/uncompleted (`Ignore / Done for today`), keeping historical integrity for personal reflection.
3. **Notification Strategy:**
   - Evening nudge (e.g., 20:00 - 21:00): "Sudah siap untuk besok? Yuk tulis rencanamu di Growth Up!"
   - Midday nudge (e.g., 12:00 or 15:00): "Bagaimana harimu? Cek kegiatanmu hari ini."
4. **Visual Theme:**
   - Primary: Soft Blush Pink (`#F48FB1` / `#F8BBD0` / `#C2185B`).
   - Background: Creamy off-white / light rose tint (`#FFF8F9` / `#FFFFFF`).
   - Surface: Clean white cards with gentle border/elevation (`#FFFFFF`, border `#FCE4EC`).
   - Text: High-contrast warm charcoal (`#2D2426`) for body text, ensuring >= 4.5:1 accessibility contrast.

---

## 5. What's Left to the Builder

- Exact database table schema (`tasks` table with `id`, `title`, `date`, `is_completed`, `status`, `created_at`).
- Implementation of Compose screens: `TodayScreen`, `PlanTomorrowScreen`, `HistoryScreen`.
- Setup of Gradle dependencies and build configuration.
- Building the signed release APK or test release build.
- Installing via ADB / Mobile-MCP to the connected device.
