package com.growthup.app.ui.tomorrow

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

data class TomorrowUiState(
    val tomorrowDate: String = LocalDate.now().plusDays(1).toString(),
    val tomorrowTasks: List<TaskEntity> = emptyList(),
    val uncompletedPreviousTasks: List<TaskEntity> = emptyList(),
    val inputTaskTitle: String = "",
    val isLoading: Boolean = false
)

class TomorrowViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TomorrowUiState())
    val uiState: StateFlow<TomorrowUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        val today = LocalDate.now().toString()
        val tomorrow = LocalDate.now().plusDays(1).toString()

        _uiState.value = _uiState.value.copy(tomorrowDate = tomorrow)

        viewModelScope.launch {
            repository.getTasksForDate(tomorrow).collectLatest { list ->
                _uiState.value = _uiState.value.copy(tomorrowTasks = list)
            }
        }

        viewModelScope.launch {
            repository.getUncompletedTasksBefore(tomorrow).collectLatest { list ->
                _uiState.value = _uiState.value.copy(uncompletedPreviousTasks = list)
            }
        }
    }

    fun onInputTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(inputTaskTitle = newTitle)
    }

    fun addTaskForTomorrow(title: String = _uiState.value.inputTaskTitle) {
        if (title.isBlank()) return
        val tomorrow = LocalDate.now().plusDays(1).toString()
        viewModelScope.launch {
            repository.addTask(title.trim(), tomorrow)
            _uiState.value = _uiState.value.copy(inputTaskTitle = "")
        }
    }

    fun rolloverTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.rolloverToTomorrow(task)
        }
    }

    fun ignoreTask(id: Long) {
        viewModelScope.launch {
            repository.archiveTask(id)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }
}
