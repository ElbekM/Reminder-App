package com.elbek.reminder.interactors

import com.elbek.reminder.common.extensions.update
import com.elbek.reminder.common.utils.Utils.findById
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
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class TaskListInteractor @Inject constructor(
    private val database: TaskListDao
) {
    private var taskLists by Delegates.observable(mutableListOf<TaskList>()) { _, _, value ->
        databaseUpdated.onNext(value)
        allDataBaseUpdated.onNext(Unit)
    }

    val databaseUpdated = PublishSubject.create<List<TaskList>>()

    val allDataBaseUpdated = PublishSubject.create<Unit>()

    fun getTaskListById(id: String): TaskList? = taskLists.findById(id)

    fun getTaskListIdByTask(taskId: String): String? {
        taskLists.forEach { taskList ->
            taskList.takeIf {
                it.tasks.find { task -> task.id == taskId }?.run { true } ?: false
            }?.let { return it.id }
        }
        return null
    }

    fun getTaskById(taskListId: String, taskId: String): Task? =
        taskLists.findById(taskListId)?.tasks?.firstOrNull { it.id == taskId }

    fun getTaskListByType(type: TaskType): Single<List<Task>> =
        database.getAllTaskLists()
            .map { taskLists ->
                val tasks = mutableListOf<Task>()
                taskLists.map { it.toModel() }.run {
                    forEach { tasks.addAll(it.tasks) }
                }
                tasks
            }
            .flatMap { tasks ->
                Observable.fromIterable(tasks)
                    .filter {
                        when (type) {
                            TaskType.IMPORTANT -> it.isImportant
                            TaskType.MY_DAY -> it.isInMyDate
                            TaskType.COMPLETED -> it.isCompleted
                            else -> false
                        }
                    }.toList()
            }

    fun getTaskLists(): Completable =
        database.getAllTaskLists()
            .map { taskLists -> taskLists.map { it.toModel() } }
            .flatMap { lists ->
                Observable.fromIterable(lists).filter { taskList ->
                    !TaskType.values().map { it.title }.contains(taskList.name)
                }.toList()
            }
            .doOnSuccess { taskLists = it }
            .ignoreElement()

    fun insertTaskList(taskList: TaskList): Completable =
        Single.fromCallable { TaskListEntity(taskList) }
            .observeOn(Schedulers.io())
            .flatMapCompletable { entity ->
                taskLists = taskLists.apply { add(taskList) }
                database.insert(entity)
            }

    fun updateTaskListName(id: String, newName: String): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply { findById(id)?.name = newName }
        }
            .flatMapCompletable { database.updateTaskListName(id, newName) }

    fun deleteTaskListById(id: String): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply { removeAll { it.id == id } }
        }
            .flatMapCompletable { database.deleteTaskList(id) }

    fun updateTasks(id: String, tasks: List<Task>): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply { findById(id)?.tasks = tasks.toMutableList() }
        }
            .observeOn(Schedulers.io())
            .map { tasks.run { map { TaskEntity(it) } } }
            .flatMapCompletable {
                database.updateTasks(id, it)
            }

    fun insertTask(id: String, task: Task): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply { findById(id)?.tasks?.add(task) }
        }
            .observeOn(Schedulers.io()) //TODO: check threads
            .map { taskLists.findById(id)?.tasks?.run { map { TaskEntity(it) } } }
            .flatMapCompletable { database.updateTasks(id, it) }

    fun updateTask(taskListId: String, task: Task): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply {
                findById(taskListId)?.tasks?.update(task) { it.id == task.id }
            }
        }
            .observeOn(Schedulers.io())
            .map { taskLists.findById(taskListId)?.tasks?.run { map { TaskEntity(it) } } }
            .flatMapCompletable { database.updateTasks(taskListId, it) }

    fun deleteTask(taskListId: String, taskId: String): Completable =
        Single.fromCallable {
            taskLists = taskLists.apply {
                findById(taskListId)?.tasks?.removeAll { it.id == taskId }
            }
        }
            .observeOn(Schedulers.io())
            .map {
                taskLists.findById(taskListId)?.tasks?.run { map { TaskEntity(it) } }
            }
            .flatMapCompletable { database.updateTasks(taskListId, it) }
}
