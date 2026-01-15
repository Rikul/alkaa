package com.escodro.task.presentation.list

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/**
 * ViewModel responsible to handle the interaction between the presentation and business logic from
 * Task Section.
 */
internal class TaskListViewModel(
    private val loadAllTasksUseCase: LoadUncompletedTasks,
    private val loadCompletedTasksUseCase: LoadCompletedTasks,
    private val updateTaskStatusUseCase: UpdateTaskStatus,
    private val applicationScope: AppCoroutineScope,
    private val taskWithCategoryMapper: TaskWithCategoryMapper,
) : ViewModel() {

    private val _showCompleted = MutableStateFlow(false)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    fun setShowCompleted(show: Boolean) {
        _showCompleted.value = show
    }

    fun loadTaskList(categoryId: Long? = null): Flow<TaskListViewState> =
        _showCompleted.flatMapLatest { showCompleted ->
            val baseFlow = loadAllTasksUseCase(categoryId = categoryId)
            if (showCompleted) {
                baseFlow.combine(loadCompletedTasksUseCase()) { uncompleted, completed ->
                    val filteredCompleted = if (categoryId != null) {
                        completed.filter { it.task.categoryId == categoryId }
                    } else {
                        completed
                    }
                    uncompleted + filteredCompleted
                }
            } else {
                baseFlow
            }
        }
            .map { task -> taskWithCategoryMapper.toView(task) }
            .map { tasks ->
                if (tasks.isNotEmpty()) {
                    TaskListViewState.Loaded(tasks)
                } else {
                    TaskListViewState.Empty
                }
            }
            .catch { error -> emit(TaskListViewState.Error(error)) }

    fun updateTaskStatus(item: TaskWithCategory) = applicationScope.launch {
        updateTaskStatusUseCase(item.task.id)
    }
}
