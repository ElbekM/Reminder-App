package com.elbek.reminder.screens.taskList

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.Command
import com.elbek.reminder.common.core.commands.DataList
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.common.core.commands.Text
import com.elbek.reminder.common.core.commands.Visible
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.interactors.TaskListInteractor
import com.elbek.reminder.models.Task
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.TaskType
import com.elbek.reminder.screens.task.TaskLaunchArgs
import com.elbek.reminder.screens.taskList.adapter.TaskClickType
import com.elbek.reminder.screens.taskList.adapter.TaskListItem
import com.elbek.reminder.screens.taskList.newTask.NewTaskLaunchArgs
import io.reactivex.Observable

class TaskListViewModel @ViewModelInject constructor(
    private val taskListInteractor: TaskListInteractor,
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private var taskList: TaskList? = null
    private var taskListType: TaskType? = null
    private var allTasks: List<Task>? = null

    val taskListNameText = Text()
    val dateTimeText = Text()

    val addNewTaskButtonVisible = Visible()
    val taskListItems = DataList<TaskListItem>()

    val setTaskListNameFocusCommand = Command()
    val openTaskScreenCommand = TCommand<TaskLaunchArgs>()
    val openNewTaskScreenCommand = TCommand<NewTaskLaunchArgs>()
    val openSettingsBottomSheetCommand = TCommand<String?>()

    fun init(args: TaskListLaunchArgs?) {
        if (args?.taskType != null) {
            taskListType = args.taskType
            when (taskListType) {
                TaskType.TASKS -> {
                    subscribeToDbUpdate()
                    defaultTaskListInteractor.getTaskList()
                        .subscribeOnIoObserveOnMain()
                        .addToSubscriptions()
                }
                else -> {
                    subscribeToTasksUpdate()
                    getAllTasks()
                }
            }
        } else {
            args?.taskListId?.let {
                taskList = taskListInteractor.getTaskListById(it)
                updateView()
            } ?: run {
                setFocusToTitle()
            }
            subscribeToDbUpdate(args?.taskListId)
        }
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
            } ?: allTasks?.get(position)?.let { task ->
                openTaskScreenCommand.call(
                    TaskLaunchArgs(
                        taskListId = taskListInteractor.getTaskListIdByTask(task.id),
                        taskId = task.id
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
        openSettingsBottomSheetCommand.call(taskList?.id)
    }

    fun onAddNewTaskClicked() {
        taskList?.let {
            openNewTaskScreenCommand.call(NewTaskLaunchArgs(taskListId = it.id))
        } ?: openNewTaskScreenCommand.call(
            NewTaskLaunchArgs(
                taskListId = defaultTaskListInteractor.getTaskListId(),
                taskListType = taskListType
            )
        )
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

    private fun subscribeToDbUpdate(taskListId: String? = null) {
        taskListInteractor.databaseUpdated
            .subscribeOnIoObserveOnMain { taskLists ->
                taskList = taskListId?.let { id ->
                    taskLists.firstOrNull { it.id == id }
                } ?: taskLists.last()
                updateView()
            }
            .addToSubscriptions()

        defaultTaskListInteractor.databaseUpdated
            .subscribeOnIoObserveOnMain {
                taskList = it
                updateView()
            }
            .addToSubscriptions()
    }

    private fun subscribeToTasksUpdate() {
        taskListInteractor.allDataBaseUpdated
            .subscribeOnIoObserveOnMain { runWithDelay({ getAllTasks() }) }
            .addToSubscriptions()
    }

    private fun getAllTasks() {
        taskListInteractor.getTaskListByType(taskListType!!)
            .subscribeOnIoObserveOnMain {
                allTasks = it
                updateView()
            }
            .addToSubscriptions()
    }

    private fun updateView() {
        taskList?.let {
            taskListNameText.value = it.name ?: ""
            setTasks(it.tasks)
        } ?: run {
            taskListNameText.value = taskListType?.title ?: ""
            setTasks(allTasks)
        }

        dateTimeText.value = "Saturday, 21 November"
        addNewTaskButtonVisible.value = true
    }

    private fun setTasks(tasks: List<Task>?) = tasks?.let { items ->
        Observable.fromIterable(items)
            .map {
                TaskListItem(
                    name = it.name,
                    taskCompleted = "",
                    isImportant = it.isImportant,
                    isCompleted = it.isCompleted
                )
            }.toList()
            .subscribeOnIoObserveOnMain { taskListItems.value = it }
            .addToSubscriptions()
    } ?: run {
        //TODO: show some empty state
    }
}
