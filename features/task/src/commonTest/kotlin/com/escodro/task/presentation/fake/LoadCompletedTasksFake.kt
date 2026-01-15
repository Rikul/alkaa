package com.escodro.task.presentation.fake

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadCompletedTasksFake : LoadCompletedTasks {

    private var list: List<TaskWithCategory> = emptyList()

    fun returnValues(values: List<TaskWithCategory>) {
        list = values
    }

    fun clean() {
        list = emptyList()
    }

    override fun invoke(): Flow<List<TaskWithCategory>> = flowOf(list)
}
