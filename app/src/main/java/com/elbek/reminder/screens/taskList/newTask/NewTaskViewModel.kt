package com.elbek.reminder.screens.taskList.newTask

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.Task
import com.elbek.reminder.screens.general.TaskType

class NewTaskViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var taskListId: String
    private var taskListType: TaskType? = null
    private var isCustomTaskList: Boolean = false

    private var isImportant: Boolean = false
    private var isInMyDay: Boolean = false

    fun init(args: NewTaskLaunchArgs) {
        this.taskListId = args.taskListId
        taskListType = args.taskListType

        isCustomTaskList = !defaultTaskListInteractor.isDefaultTaskList(taskListId)
        isImportant = taskListType == TaskType.IMPORTANT
        isInMyDay = taskListType == TaskType.MY_DAY
    }

    fun onAddTaskClicked(taskName: String) {
        if (isCustomTaskList) {
            taskListInteractor.insertTask(taskListId, Task(name = taskName))
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.insertTask(
                taskListId,
                Task(
                    name = taskName,
                    isImportant = isImportant,
                    isInMyDate = isInMyDay
                )
            )
                .subscribeOnIoObserveOnMain { closeCommand.call() }
                .addToSubscriptions()
        }
    }
}
