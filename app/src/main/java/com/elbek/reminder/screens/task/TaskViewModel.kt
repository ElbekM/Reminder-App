package com.elbek.reminder.screens.task

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Data
import com.elbek.reminder.common.core.commands.Text
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.Task
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

    val isImportant = Data(false)

    fun init(launchArgs: TaskLaunchArgs) {
        taskListId = launchArgs.taskListId ?: defaultTaskListInteractor.getTaskListId()
        taskId = launchArgs.taskId

        task = taskListInteractor.getTaskById(taskListId, taskId) ?: run {
            isCustomTaskList = false
            defaultTaskListInteractor.getTaskById(taskId)
        } ?: throw NullPointerException()

        updateView()
        //TODO: subscribe to Subjects
    }

    fun onAddNewSubTaskClicked() {
        //TODO: add new subtasks
    }

    fun onImportantClicked() {
        task.isImportant = !task.isImportant
        isImportant.value = task.isImportant

        if (isCustomTaskList) {
            taskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain()
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain()
                .addToSubscriptions()
        }
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

    fun onCompleteClicked(name: String, notes: String) {
        task.name = name
        task.description = notes

        if (isCustomTaskList) {
            taskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        }
    }

    private fun updateView() {
        //TODO: st correct toolbar title
        toolbarTitleText.value = "List"
        taskNameText.value = task.name
        taskNotesText.value = task.description ?: ""
        isImportant.value = task.isImportant
    }
}
