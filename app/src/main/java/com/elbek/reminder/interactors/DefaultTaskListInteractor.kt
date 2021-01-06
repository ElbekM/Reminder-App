package com.elbek.reminder.interactors

import com.elbek.reminder.common.extensions.update
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
class DefaultTaskListInteractor @Inject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val database: TaskListDao
) {
    private var taskList by Delegates.observable(TaskList(name = null)) { _, _, value ->
        databaseUpdated.onNext(value)
        taskListInteractor.allDataBaseUpdated.onNext(Unit)
    }

    val databaseUpdated = PublishSubject.create<TaskList>()

    fun getTaskById(taskId: String) = taskList.tasks.firstOrNull { it.id == taskId }

    fun getTaskListId(): String = taskList.id

    //TODO: refactor filter
    fun getTaskList(): Completable =
        database.getAllTaskLists()
            .map { taskLists -> taskLists.map { it.toModel() } }
            .flatMap { lists ->
                Observable.fromIterable(lists).filter { taskList ->
                    TaskType.values().map { it.title }.contains(taskList.name)
                }.toList()
            }
            .doOnSuccess { taskList = it.first() }
            .ignoreElement()

    fun insertTaskList(taskList: TaskList): Completable =
        Single.fromCallable { TaskListEntity(taskList) }
            .observeOn(Schedulers.io())
            .flatMapCompletable { entity ->
                this.taskList = taskList
                database.insert(entity)
            }

    fun insertTask(id: String, task: Task): Completable =
        Single.fromCallable {
            taskList = taskList.apply { tasks.add(task) }
        }
            .observeOn(Schedulers.io()) //TODO: check threads
            .map { taskList.tasks.run { map { TaskEntity(it) } } }
            .flatMapCompletable { database.updateTasks(id, it) }

    fun updateTask(taskListId: String, task: Task): Completable =
        Single.fromCallable {
            taskList = taskList.apply {
                tasks.update(task) { it.id == task.id }
            }
        }
            .observeOn(Schedulers.io())
            .map { taskList.tasks.run { map { TaskEntity(it) } } }
            .flatMapCompletable { database.updateTasks(taskListId, it) }

    fun deleteTask(taskListId: String, taskId: String): Completable =
        Single.fromCallable {
            taskList = taskList.apply {
                tasks.removeAll { it.id == taskId }
            }
        }
            .observeOn(Schedulers.io())
            .map {
                taskList.tasks.run { map { TaskEntity(it) } }
            }
            .flatMapCompletable { database.updateTasks(taskListId, it) }

    fun isDefaultTaskList(id: String): Boolean = id == taskList.id
}
