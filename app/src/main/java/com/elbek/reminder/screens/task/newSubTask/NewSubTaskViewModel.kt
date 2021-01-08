package com.elbek.reminder.screens.task.newSubTask

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.SubTask
import com.elbek.reminder.models.Task
import com.elbek.reminder.screens.task.TaskLaunchArgs
import java.lang.NullPointerException

class NewSubTaskViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var task: Task
    private lateinit var taskListId: String
    private var isCustomTaskList: Boolean = true

    fun init(args: TaskLaunchArgs) {
        taskListId = args.taskListId!!

        task = taskListInteractor.getTaskById(taskListId, args.taskId) ?: run {
            isCustomTaskList = false
            defaultTaskListInteractor.getTaskById(args.taskId)
        } ?: throw NullPointerException()
    }

    fun onAddSubTaskClicked(subTaskName: String) {
        task.subTasks.add(SubTask(name = subTaskName))

        if (isCustomTaskList) {
            taskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain(closeCommand)
                .addToSubscriptions()
        } else {
            defaultTaskListInteractor.updateTask(taskListId, task)
                .subscribeOnIoObserveOnMain(closeCommand)
                .addToSubscriptions()
        }
    }
}
