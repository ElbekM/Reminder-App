package com.elbek.reminder.screens.taskList.settings

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.Enabled
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor

class TaskListSettingsViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var taskListId: String
    private var isCustomTaskList: Boolean = true

    val renameButtonEnabled = Enabled()
    val deleteButtonEnabled = Enabled()
    val taskListRemoved = Command()

    fun init(taskListId: String?) {
        //TODO: Tasks list comes with id
        this.taskListId = taskListId ?: run {
            isCustomTaskList = false
            defaultTaskListInteractor.getTaskListId()
        }

        renameButtonEnabled.value = isCustomTaskList
        deleteButtonEnabled.value = isCustomTaskList
    }

    fun onDeleteClicked() {
        taskListInteractor.deleteTaskListById(taskListId)
            .subscribeOnIoObserveOnMain { taskListRemoved.call() }
            .addToSubscriptions()
    }
}
