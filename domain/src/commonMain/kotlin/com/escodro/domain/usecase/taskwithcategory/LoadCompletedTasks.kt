package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get all completed tasks from the database.
 */
interface LoadCompletedTasks {

    /**
     * Gets all completed tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Flow<List<TaskWithCategory>>
}
