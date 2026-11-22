# Graph Report - growth up  (2026-11-22)

## Corpus Check
- Corpus is ~9,586 words - fits in a single context window. You may not need a graph.

## Summary
- 337 nodes · 784 edges · 14 communities (7 shown, 7 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 24 edges (avg confidence: 0.78)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Cluster 0
- Cluster 1
- Cluster 2
- Cluster 3
- Cluster 4
- Cluster 5
- Cluster 6
- Cluster 8
- Cluster 9

## God Nodes (most connected - your core abstractions)
1. `TaskEntity` - 57 edges
2. `TaskRepository` - 23 edges
3. `TomorrowViewModel` - 19 edges
4. `TaskRepositoryImpl` - 18 edges
5. `TaskDao` - 17 edges
6. `TodayViewModel` - 16 edges
7. `FakeReactiveTaskDao` - 14 edges
8. `GrowthUpApp()` - 13 edges
9. `TomorrowScreen()` - 13 edges
10. `FakeTaskDao` - 12 edges

## Surprising Connections (you probably didn't know these)
- `GrowthUpDatabase` --references--> `TaskEntity`  [EXTRACTED]
  app/src/main/java/com/growthup/app/data/local/GrowthUpDatabase.kt → app/src/main/java/com/growthup/app/data/model/TaskEntity.kt
- `RolloverItem()` --references--> `TaskEntity`  [EXTRACTED]
  app/src/main/java/com/growthup/app/ui/components/RolloverSection.kt → app/src/main/java/com/growthup/app/data/model/TaskEntity.kt
- `TaskCard()` --references--> `TaskEntity`  [EXTRACTED]
  app/src/main/java/com/growthup/app/ui/components/TaskCard.kt → app/src/main/java/com/growthup/app/data/model/TaskEntity.kt
- `HistoryUiState` --references--> `TaskEntity`  [EXTRACTED]
  app/src/main/java/com/growthup/app/ui/history/HistoryViewModel.kt → app/src/main/java/com/growthup/app/data/model/TaskEntity.kt
- `TodayUiState` --references--> `TaskEntity`  [EXTRACTED]
  app/src/main/java/com/growthup/app/ui/today/TodayViewModel.kt → app/src/main/java/com/growthup/app/data/model/TaskEntity.kt

## Import Cycles
- None detected.

## Communities (14 total, 7 thin omitted)

### Community 0 - "Cluster 0"
Cohesion: 0.07
Nodes (8): TaskDao, TaskEntity, TaskRepositoryImpl, HistorySummary, TaskRepository, FakeTaskDao, TaskRepositoryTest, FakeReactiveTaskDao

### Community 1 - "Cluster 1"
Cohesion: 0.07
Nodes (4): DailyProgressBar(), RolloverItem(), TaskCard(), GrowthUpColors

### Community 2 - "Cluster 2"
Cohesion: 0.08
Nodes (4): SanityTest, TaskEntityTest, TodayViewModelTest, TomorrowViewModelTest

### Community 3 - "Cluster 3"
Cohesion: 0.09
Nodes (9): HistoryCalculator, HistoryStats, HistoryUiState, HistoryViewModel, TodayScreen(), TodayUiState, TodayViewModel, TomorrowUiState (+1 more)

### Community 4 - "Cluster 4"
Cohesion: 0.11
Nodes (5): MainActivity, GrowthUpApp(), GrowthUpTheme(), TomorrowScreen(), TomorrowViewModel

### Community 5 - "Cluster 5"
Cohesion: 0.11
Nodes (3): NotificationHelper, ReminderReceiver, NotificationHelperTest

### Community 6 - "Cluster 6"
Cohesion: 0.10
Nodes (6): HistoryScreen(), StatCard(), Screen, HISTORY, TODAY, TOMORROW

## Knowledge Gaps
- **3 isolated node(s):** `TODAY`, `TOMORROW`, `HISTORY`
  These have ≤1 connection - possible missing edges. (Counts symbols only; 76 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **7 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TaskEntity` connect `Cluster 0` to `Cluster 1`, `Cluster 2`, `Cluster 3`, `Cluster 4`, `Cluster 8`?**
  _High betweenness centrality (0.311) - this node is a cross-community bridge._
- **What connects `TODAY`, `TOMORROW`, `HISTORY` to the rest of the system?**
  _3 weakly-connected nodes found - possible missing edges._
- **Should `Cluster 0` be split into smaller, more focused modules?**
  _Cohesion score 0.06842105263157895 - nodes in this community are weakly interconnected._
- **Why does `TomorrowViewModel` connect `Cluster 4` to `Cluster 0`, `Cluster 2`, `Cluster 3`, `Cluster 6`?**
  _High betweenness centrality (0.071) - this node is a cross-community bridge._
- **Should `Cluster 1` be split into smaller, more focused modules?**
  _Cohesion score 0.07420091324200913 - nodes in this community are weakly interconnected._
- **Why does `TodayViewModel` connect `Cluster 3` to `Cluster 0`, `Cluster 2`, `Cluster 4`, `Cluster 6`?**
  _High betweenness centrality (0.064) - this node is a cross-community bridge._
- **Should `Cluster 2` be split into smaller, more focused modules?**
  _Cohesion score 0.08253968253968254 - nodes in this community are weakly interconnected._