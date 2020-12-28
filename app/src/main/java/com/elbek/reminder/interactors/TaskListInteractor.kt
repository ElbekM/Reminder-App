package com.elbek.reminder.interactors

import com.elbek.reminder.database.TaskListDao
import com.elbek.reminder.database.entities.TaskEntity
import com.elbek.reminder.database.entities.TaskListEntity
import com.elbek.reminder.models.Task
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.TaskType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskListInteractor @Inject constructor(
    private val database: TaskListDao
) {

    fun getAllTaskLists(): Single<List<TaskList>> =
        database.getAllTaskLists()
            .map { taskLists -> taskLists.map { it.toModel() } }

    fun getTaskListById(id: String): Single<TaskList> =
        database.getTaskList(id).map { it.toModel() }

    fun insertTaskList(taskList: TaskList): Completable =
        Single.fromCallable { TaskListEntity(taskList) }
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                database.insert(it)
            }

    fun insertTaskLists(taskLists: List<TaskList>): Completable =
        Single.fromCallable {
            taskLists.run { map { TaskListEntity(it) } }
        }
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                database.insert(it)
            }

    fun deleteTaskListById(id: String): Completable =
        database.deleteTaskList(id)

    fun updateTaskListName(id: String, newName: String): Completable =
        database.updateTaskListName(id, newName)

    fun updateTasks(id: String, tasks: List<Task>): Completable =
        Single.fromCallable {
            tasks.run { map { TaskEntity(it) } }
        }
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                database.updateTasks(id, it)
            }

    fun getTaskLists(taskLists: List<TaskList>): Single<List<TaskList>> =
        Single.just(taskLists)
            .flatMap { lists ->
                Observable.fromIterable(lists).filter { taskList ->
                    !TaskType.values().map { it.title }.contains(taskList.name)
                }.toList()
            }

    fun getDefaultTaskLists(taskLists: List<TaskList>): Single<List<TaskList>> =
        Single.just(taskLists)
            .flatMap { lists ->
                Observable.fromIterable(lists).filter { taskList ->
                    TaskType.values().map { it.title }.contains(taskList.name)
                }.toList()
            }
}
