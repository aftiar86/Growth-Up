package com.growthup.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.data.model.UnlockedRewardEntity
import com.growthup.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HistoryStats(
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val percentage: Int = 0,
    val daysTracked: Int = 0
)

object HistoryCalculator {
    fun calculateStats(tasks: List<TaskEntity>): HistoryStats {
        if (tasks.isEmpty()) return HistoryStats()
        val total = tasks.size
        val completed = tasks.count { it.isCompleted }
        val pct = if (total > 0) (completed * 100) / total else 0
        val days = tasks.map { it.targetDate }.distinct().size
        return HistoryStats(
            totalCount = total,
            completedCount = completed,
            percentage = pct,
            daysTracked = days
        )
    }
}

data class HistoryUiState(
    val stats: HistoryStats = HistoryStats(),
    val allTasks: List<TaskEntity> = emptyList(),
    val tasksByDate: Map<String, List<TaskEntity>> = emptyMap(),
    val unlockedRewards: List<UnlockedRewardEntity> = emptyList(),
    val isLoading: Boolean = false
)

class HistoryViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            repository.getAllTasks().collectLatest { tasks ->
                val stats = HistoryCalculator.calculateStats(tasks)
                val grouped = tasks.groupBy { it.targetDate }
                _uiState.value = _uiState.value.copy(
                    stats = stats,
                    allTasks = tasks,
                    tasksByDate = grouped,
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            repository.getAllUnlockedRewards().collectLatest { rewards ->
                _uiState.value = _uiState.value.copy(unlockedRewards = rewards)
            }
        }
    }
}
