package com.elbek.reminder.screens.general

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.DataList
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.interactors.CustomTaskListInteractor
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.adapters.TaskCardItem
import com.elbek.reminder.screens.general.adapters.TaskCardType
import com.elbek.reminder.screens.general.adapters.TaskTypeItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeneralViewModel @ViewModelInject constructor(
    private val taskListInteractor: CustomTaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private var taskLists: MutableList<TaskList> = mutableListOf()
    private var defaultTaskLists: MutableList<TaskList> = mutableListOf()

    val taskTypes = DataList<TaskTypeItem>()
    val taskCards = DataList<TaskCardItem>()
    val createNewTaskListCommand = Command()
    val openTaskListScreenCommand = TCommand<String>()

    fun init() {
        taskListInteractor.databaseUpdated
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskLists = it.toMutableList()
                setupTaskCards()
            }, {})
            .addToSubscriptions()

        defaultTaskListInteractor.databaseUpdated
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                defaultTaskLists = it.toMutableList()
                setupTaskTypes()

                openTaskListScreenCommand.call(defaultTaskLists[0].id)
            }, {})
            .addToSubscriptions()

        loadData()
    }

    fun loadData() {
        taskListInteractor.getTaskLists()
            .andThen(defaultTaskListInteractor.getTaskLists())
            .subscribeOnIoObserveOnMain()
            .addToSubscriptions()
    }

    fun onTaskTypeClicked(position: Int) {
        val taskListId = defaultTaskLists[position].id
        openTaskListScreenCommand.call(taskListId)
    }

    fun onTaskCardClicked(cardType: TaskCardType, position: Int) {
        when (cardType) {
            TaskCardType.TASK_LIST -> {
                val taskListId = taskLists[position].id
                openTaskListScreenCommand.call(taskListId)
            }
            TaskCardType.ADD -> {
                createNewTaskListCommand.call()
            }
        }
    }

    private fun setupTaskTypes() {
        Observable.fromIterable(defaultTaskLists)
            .map {
                TaskTypeItem(
                    icon = it.icon,
                    title = it.name,
                    taskCount = it.tasks?.size ?: 0
                )
            }.toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskTypes.value = it
            }, { logError(it) })
            .addToSubscriptions()
    }

    private fun setupTaskCards() {
        //TODO: get progress, check publish subjects
        Observable.fromIterable(taskLists)
            .map {
                TaskCardItem(
                    icon = it.icon,
                    title = it.name,
                    taskCount = it.tasks?.size ?: 0,
                    progress = 0
                )
            }.toList()
            .map { items ->
                items.also { it.add(TaskCardItem(cardType = TaskCardType.ADD)) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskCards.value = it
            }, { logError(it) })
            .addToSubscriptions()
    }
}
