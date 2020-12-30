package com.elbek.reminder.interactors

import com.elbek.reminder.database.TaskListDao
import com.elbek.reminder.database.entities.TaskListEntity
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.TaskType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class DefaultTaskListInteractor @Inject constructor(
    private val database: TaskListDao
) {
    private var taskLists by Delegates.observable(mutableListOf<TaskList>()) { _, _, value ->
        databaseUpdated.onNext(value)
    }

    val databaseUpdated = PublishSubject.create<List<TaskList>>()

    fun getTaskListById(id: String): TaskList? =
        taskLists.firstOrNull { it.id == id }

    fun getTaskLists(): Completable =
        database.getAllTaskLists()
            .map { taskLists -> taskLists.map { it.toModel() } }
            .flatMap { lists ->
                Observable.fromIterable(lists).filter { taskList ->
                    TaskType.values().map { it.title }.contains(taskList.name)
                }.toList()
            }
            .doOnSuccess { taskLists = it }
            .ignoreElement()

    fun insertTaskLists(list: List<TaskList>): Completable =
        Single.fromCallable {
            list.run { map { TaskListEntity(it) } }
        }
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                taskLists = taskLists.apply { addAll(list) }
                database.insert(it)
            }
}
