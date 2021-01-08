package com.elbek.reminder.screens.general

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.DataList
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.adapters.TaskCardItem
import com.elbek.reminder.screens.general.adapters.TaskCardType
import com.elbek.reminder.screens.general.adapters.TaskTypeItem
import com.elbek.reminder.screens.taskList.TaskListLaunchArgs
import io.reactivex.Observable

class GeneralViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private var taskLists: MutableList<TaskList> = mutableListOf()
    private var defaultTaskList: TaskList? = null

    val taskTypes = DataList<TaskTypeItem>()
    val taskCards = DataList<TaskCardItem>()

    val createNewTaskListCommand = Command()
    val openTaskListScreenCommand = TCommand<TaskListLaunchArgs>()

    fun init() {
        taskListInteractor.databaseUpdated
            .subscribeOnIoObserveOnMain { taskLists = it.toMutableList() }
            .addToSubscriptions()

        defaultTaskListInteractor.databaseUpdated
            .subscribeOnIoObserveOnMain { defaultTaskList = it }
            .addToSubscriptions()

        taskListInteractor.allDataBaseUpdated
            .subscribeOnIoObserveOnMain {
                setupTaskTypes()
                setupTaskCards()
            }
            .addToSubscriptions()

        loadData()
    }

    fun loadData() {
        taskListInteractor.getTaskLists()
            .andThen(defaultTaskListInteractor.getTaskList())
            .subscribeOnIoObserveOnMain()
            .addToSubscriptions()
    }

    fun onTaskTypeClicked(taskType: TaskType) {
        openTaskListScreenCommand.call(
            TaskListLaunchArgs(taskType = taskType)
        )
    }

    fun onTaskCardClicked(cardType: TaskCardType, position: Int) {
        when (cardType) {
            TaskCardType.TASK_LIST -> {
                val taskListId = taskLists[position].id
                openTaskListScreenCommand.call(
                    TaskListLaunchArgs(taskListId = taskListId)
                )
            }
            TaskCardType.ADD -> {
                createNewTaskListCommand.call()
            }
        }
    }

    private fun setupTaskTypes() {
        //TODO: refactor enums
        Observable.fromIterable(TaskType.values().toList())
            .map {
                TaskTypeItem(
                    icon = it.icon,
                    title = it.title,
                    taskCount = getTaskCountByType(it),
                    type = it
                )
            }.toList()
            .subscribeOnIoObserveOnMain { taskTypes.value = it }
            .addToSubscriptions()
    }

    private fun setupTaskCards() {
        Observable.fromIterable(taskLists)
            .map {
                TaskCardItem(
                    icon = it.icon,
                    title = it.name,
                    taskCount = it.tasks.filter { task -> !task.isCompleted }.size,
                    progress = it.tasks.let { tasks ->
                        val completedTasks = tasks.filter { task -> task.isCompleted }.size
                        (completedTasks * 1.0 / tasks.size) * 100
                    }.toInt()
                )
            }.toList()
            .map { items ->
                items.also { it.add(TaskCardItem(cardType = TaskCardType.ADD)) }
            }
            .subscribeOnIoObserveOnMain { taskCards.value = it }
            .addToSubscriptions()
    }

    private fun getTaskCountByType(taskType: TaskType): Int =
        taskListInteractor.getTasksCountByType(taskType) + defaultTaskListInteractor.getTasksCountByType(taskType)
}
