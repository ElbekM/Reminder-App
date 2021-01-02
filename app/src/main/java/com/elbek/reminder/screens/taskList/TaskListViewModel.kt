package com.elbek.reminder.screens.taskList

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.DataList
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.common.core.commands.Text
import com.elbek.reminder.common.core.commands.Visible
import com.elbek.reminder.interactors.CustomTaskListInteractor
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.task.TaskLaunchArgs
import com.elbek.reminder.screens.taskList.adapter.TaskClickType
import com.elbek.reminder.screens.taskList.adapter.TaskListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskListViewModel @ViewModelInject constructor(
    private val taskListInteractor: CustomTaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private var taskList: TaskList? = null

    val taskListNameText = Text()
    val dateTimeText = Text()

    val addNewTaskButtonVisible = Visible()
    val taskListItems = DataList<TaskListItem>()

    val setTaskListNameFocusCommand = Command()
    val openTaskScreenCommand = TCommand<TaskLaunchArgs>()
    val openNewTaskScreenCommand = TCommand<String>()
    val openSettingsBottomSheetCommand = TCommand<String>()

    fun init(taskListId: String?) {
        taskListId?.let {
            taskList = taskListInteractor.getTaskListById(it) ?: run {
                defaultTaskListInteractor.getTaskListById(it)
            }
            updateView()
        } ?: run {
            setFocusToTitle()
        }

        taskListInteractor.databaseUpdated
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ taskLists ->
                taskList = taskListId?.let { id ->
                    taskLists.firstOrNull { it.id == id }
                } ?: taskLists.last()
                updateView()
            }, {})
            .addToSubscriptions()

        defaultTaskListInteractor.databaseUpdated
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ taskLists ->
                taskList = taskListId?.let { id ->
                    taskLists.firstOrNull { it.id == id }
                } ?: taskLists.last()
                updateView()
            }, {})
            .addToSubscriptions()
    }

    fun onTaskClicked(position: Int, type: TaskClickType) = when (type) {
        TaskClickType.TASK -> {
            taskList?.let {
                openTaskScreenCommand.call(
                    TaskLaunchArgs(
                        taskListId = it.id,
                        taskId = it.tasks[position].id
                    )
                )
            }
        }
        TaskClickType.CHECKBOX -> {
            //TODO: implement checkbox logic
        }
        TaskClickType.IMPORTANT -> {
            //TODO: implement important logic
        }
    }

    fun onSettingsClicked() {
        taskList?.let {
            openSettingsBottomSheetCommand.call(it.id)
        }
    }

    fun onAddNewTaskClicked() {
        taskList?.let {
            openNewTaskScreenCommand.call(it.id)
        }
    }

    fun onTaskListNameUpdated(taskListName: String?) {
        addNewTaskButtonVisible.value = true
        if (taskList == null) {
            taskListInteractor.insertTaskList(TaskList(name = taskListName))
                .subscribeOnIoObserveOnMain()
                .addToSubscriptions()
        } else {
            taskListInteractor.updateTaskListName(taskList!!.id, taskListName!!)
                .subscribeOnIoObserveOnMain()
                .addToSubscriptions()
        }
    }

    fun setFocusToTitle() {
        addNewTaskButtonVisible.value = false
        setTaskListNameFocusCommand.call()
    }

    private fun updateView() {
        //TODO: set current date, count compl tasks
        taskListNameText.value = taskList?.name ?: ""
        dateTimeText.value = "Saturday, 21 November"
        addNewTaskButtonVisible.value = true

        taskList?.tasks?.let { tasks ->
            Observable.fromIterable(tasks)
                .map {
                    TaskListItem(
                        name = it.name,
                        taskCompleted = "",
                        isImportant = it.isImportant,
                        isCompleted = it.isCompleted
                    )
                }.toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    taskListItems.value = it
                }, {
                    logError(it)
                })
                .addToSubscriptions()
        } ?: run {
            //TODO: show some empty state
        }
    }
}
