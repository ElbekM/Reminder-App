package com.elbek.reminder.screens.task

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Data
import com.elbek.reminder.common.core.commands.DataList
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.common.core.commands.Text
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.SubTask
import com.elbek.reminder.models.Task
import com.elbek.reminder.screens.task.adapter.SubTaskClickType
import com.elbek.reminder.screens.task.adapter.SubTaskListItem
import io.reactivex.Observable
import java.lang.NullPointerException

class TaskViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var taskListId: String
    private lateinit var taskId: String
    private lateinit var task: Task

    private var isCustomTaskList: Boolean = true

    val toolbarTitleText = Text()
    val taskNameText = Text()
    val taskNotesText = Text()

    val isTaskCompleted = Data(false)
    val isImportant = Data(false)
    val isInMyDay = Data(false)

    val subTaskItems = DataList<SubTaskListItem>()
    val openNewSubTaskScreenCommand = TCommand<TaskLaunchArgs>()

    fun init(launchArgs: TaskLaunchArgs) {
        taskListId = launchArgs.taskListId ?: defaultTaskListInteractor.getTaskListId()
        taskId = launchArgs.taskId

        taskListInteractor.allDataBaseUpdated
            .subscribeOnIoObserveOnMain { getTaskFromDatabase() }
            .addToSubscriptions()

        getTaskFromDatabase()
    }

    fun onSubTaskClicked(position: Int, clickType: SubTaskClickType) {
        task.subTasks.let { subTasks ->
            when (clickType) {
                SubTaskClickType.CHECKBOX -> subTasks[position].isCompleted = !(subTasks[position].isCompleted)
                SubTaskClickType.REMOVE -> subTasks.removeAt(position)
            }
        }
        updateTask()
    }

    fun onAddNewSubTaskClicked() {
        openNewSubTaskScreenCommand.call(
            TaskLaunchArgs(taskListId, taskId)
        )
    }

    fun onTaskCheckboxClicked() {
        task.isCompleted = !task.isCompleted
        isTaskCompleted.value = task.isCompleted

        updateTask()
    }

    fun onImportantClicked() {
        task.isImportant = !task.isImportant
        isImportant.value = task.isImportant

        updateTask()
    }

    fun onAddToMyDayClicked() {
        task.isInMyDate = !task.isInMyDate
        isInMyDay.value = task.isInMyDate

        updateTask()
    }

    fun onCompleteClicked(name: String, notes: String) {
        task.name = name
        task.description = notes

        updateTask(closeCommand)
    }

    fun onDeleteTaskClicked() {
        if (isCustomTaskList) {
            taskListInteractor.deleteTask(taskListId, taskId)
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.deleteTask(taskListId, taskId)
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        }
    }

    private fun getTaskFromDatabase() {
        task = taskListInteractor.getTaskById(taskListId, taskId) ?: run {
            isCustomTaskList = false
            defaultTaskListInteractor.getTaskById(taskId)
        } ?: throw NullPointerException()

        updateView()
    }

    private fun updateTask(block: () -> Unit = {}) {
        if (isCustomTaskList) {
            taskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain(block)
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain(block)
                .addToSubscriptions()
        }
    }

    private fun updateView() {
        //TODO: st correct toolbar title
        toolbarTitleText.value = "List"
        taskNameText.value = task.name
        taskNotesText.value = task.description ?: ""
        isTaskCompleted.value = task.isCompleted
        isImportant.value = task.isImportant
        isInMyDay.value = task.isInMyDate

        setSubTasks(task.subTasks)
    }

    private fun setSubTasks(subTasks: List<SubTask>?) = subTasks?.let { items ->
        Observable.fromIterable(items)
            .map {
                SubTaskListItem(
                    name = it.name,
                    isCompleted = it.isCompleted
                )
            }.toList()
            .subscribeOnIoObserveOnMain {
                subTaskItems.value = it
            }
            .addToSubscriptions()
    } ?: run {
        //TODO: show some empty state
    }
}
