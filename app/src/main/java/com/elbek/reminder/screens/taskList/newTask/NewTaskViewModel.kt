package com.elbek.reminder.screens.taskList.newTask

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.interactors.CustomTaskListInteractor
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.models.Task

class NewTaskViewModel @ViewModelInject constructor(
    private val taskListInteractor: CustomTaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var taskListId: String
    private var isCustomTaskList: Boolean = false

    fun init(taskListId: String) {
        this.taskListId = taskListId

        isCustomTaskList = taskListInteractor.getTaskListById(taskListId)?.run { true } ?: false
    }

    fun onAddTaskClicked(taskName: String) {
        if (isCustomTaskList) {
            taskListInteractor.insertTask(taskListId, Task(name = taskName))
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.insertTask(taskListId, Task(name = taskName))
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        }
    }
}
