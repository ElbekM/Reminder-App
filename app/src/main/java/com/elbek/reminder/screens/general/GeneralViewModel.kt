package com.elbek.reminder.screens.general

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.DataList
import com.elbek.reminder.common.core.TCommand
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.adapters.TaskCardItem
import com.elbek.reminder.screens.general.adapters.TaskCardType
import com.elbek.reminder.screens.general.adapters.TaskTypeItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeneralViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private var taskLists: List<TaskList> = listOf()

    val taskTypes = DataList<TaskTypeItem>()
    val taskCards = DataList<TaskCardItem>()
    val openTaskListScreenCommand = TCommand<String>()

    fun init() {
        taskListInteractor.getAllTaskLists()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskLists = it
                setupTaskTypes()
                setupTaskCards()

                openTaskListScreenCommand.call(taskLists[0].id)
            }, {})
            .addToSubscriptions()
    }

    fun onTaskTypeClicked(position: Int) {
        val taskListId = taskLists[position].id
        openTaskListScreenCommand.call(taskListId)
    }

    fun onTaskCardClicked(cardType: TaskCardType, position: Int) {
        when (cardType) {
            TaskCardType.TASK_LIST -> {
                val offset = TaskType.values().size + position
                val taskListId = taskLists[offset].id
                openTaskListScreenCommand.call(taskListId)
            }
            TaskCardType.ADD -> {
                //TODO: Add new task list
            }
        }
    }

    private fun setupTaskTypes() {
        taskListInteractor.getDefaultTaskLists(taskLists)
            .subscribeOn(Schedulers.io())
            .flatMap { taskLists ->
                Observable.fromIterable(taskLists)
                    .map {
                        TaskTypeItem(
                            icon = it.icon,
                            title = it.name,
                            taskCount = it.tasks?.size ?: 0
                        )
                    }.toList()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskTypes.value = it
            }, { logError(it) })
            .addToSubscriptions()
    }

    private fun setupTaskCards() {
        //TODO: get progress, check publish subjects
        taskListInteractor.getTaskLists(taskLists)
            .subscribeOn(Schedulers.io())
            .flatMap { taskLists ->
                Observable.fromIterable(taskLists)
                    .map {
                        TaskCardItem(
                            icon = it.icon,
                            title = it.name,
                            taskCount = it.tasks?.size ?: 0,
                            progress = 0
                        )
                    }.toList()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskCards.value = it
            }, { logError(it) })
            .addToSubscriptions()
    }
}
