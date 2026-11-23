package com.growthup.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.domain.model.DollCatalog
import com.growthup.app.domain.model.DollEvolution
import com.growthup.app.domain.model.DollEvolutionCalculator
import com.growthup.app.domain.model.DollReward
import com.growthup.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TodayUiState(
    val date: String = LocalDate.now().toString(),
    val tasks: List<TaskEntity> = emptyList(),
    val inputTaskTitle: String = "",
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progressPercent: Int = 0,
    val isLoading: Boolean = false,
    val celebrationReward: DollReward? = null,
    val celebrationEvolution: DollEvolution? = null,
    val showCelebrationDialog: Boolean = false
)

class TodayViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private var hasShownCelebrationToday: Boolean = false

    init {
        loadTasks()
    }

    fun loadTasks() {
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repository.getTasksForDate(today).collectLatest { taskList ->
                val total = taskList.size
                val completed = taskList.count { it.isCompleted }
                val percent = if (total > 0) (completed * 100) / total else 0

                val isCompleted100 = total > 0 && completed == total
                var rewardToCelebrate: DollReward? = _uiState.value.celebrationReward
                var evolutionToCelebrate: DollEvolution? = _uiState.value.celebrationEvolution
                var showCelebration = _uiState.value.showCelebrationDialog

                if (isCompleted100 && !hasShownCelebrationToday) {
                    val existing = repository.getRewardForDate(today)
                    val doll = if (existing == null) {
                        val d = DollCatalog.getRewardForDate(today)
                        repository.unlockRewardForDate(today, d.id)
                        d
                    } else {
                        DollCatalog.getDollById(existing.dollId) ?: DollCatalog.getRewardForDate(today)
                    }

                    val allRewards = try {
                        repository.getAllUnlockedRewards().first()
                    } catch (e: Exception) {
                        emptyList()
                    }
                    val dollUnlocks = allRewards.count { it.dollId == doll.id }.coerceAtLeast(1)
                    rewardToCelebrate = doll
                    evolutionToCelebrate = DollEvolutionCalculator.calculateEvolution(doll.id, dollUnlocks)
                    showCelebration = true
                    hasShownCelebrationToday = true
                }

                _uiState.value = _uiState.value.copy(
                    date = today,
                    tasks = taskList,
                    totalCount = total,
                    completedCount = completed,
                    progressPercent = percent,
                    celebrationReward = rewardToCelebrate,
                    celebrationEvolution = evolutionToCelebrate,
                    showCelebrationDialog = showCelebration,
                    isLoading = false
                )
            }
        }
    }

    fun dismissCelebrationDialog() {
        _uiState.value = _uiState.value.copy(showCelebrationDialog = false)
    }

    fun onInputTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(inputTaskTitle = newTitle)
    }

    fun addTaskForToday(title: String = _uiState.value.inputTaskTitle) {
        if (title.isBlank()) return
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repository.addTask(title.trim(), today)
            _uiState.value = _uiState.value.copy(inputTaskTitle = "")
        }
    }

    fun toggleTask(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTask(id, isCompleted)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }
}
