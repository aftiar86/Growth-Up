package com.growthup.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growthup.app.data.model.TaskEntity
import com.growthup.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TodayUiState(
    val date: String = LocalDate.now().toString(),
    val tasks: List<TaskEntity> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progressPercent: Int = 0,
    val isLoading: Boolean = false
)

class TodayViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

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
                _uiState.value = _uiState.value.copy(
                    date = today,
                    tasks = taskList,
                    totalCount = total,
                    completedCount = completed,
                    progressPercent = percent,
                    isLoading = false
                )
            }
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
