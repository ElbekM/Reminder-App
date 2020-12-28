package com.elbek.reminder.repositories

import com.elbek.reminder.database.TaskListDao
import com.elbek.reminder.database.entities.TaskListEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskListRepository @Inject constructor(
    private val taskListDb: TaskListDao
) {

    fun getAllTaskLists(): Single<List<TaskListEntity>> =
        taskListDb.getAllTaskLists()

    fun insertTaskLists(taskLists: List<TaskListEntity>): Completable =
        taskListDb.insert(taskLists)
}
