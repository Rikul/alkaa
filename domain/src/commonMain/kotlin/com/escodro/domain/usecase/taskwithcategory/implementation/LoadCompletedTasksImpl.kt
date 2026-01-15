package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LoadCompletedTasksImpl(
    private val repository: TaskWithCategoryRepository,
) : LoadCompletedTasks {

    override fun invoke(): Flow<List<TaskWithCategory>> =
        repository
            .findAllTasksWithCategory()
            .map { list -> list.filter { item -> item.task.isCompleted } }
}
