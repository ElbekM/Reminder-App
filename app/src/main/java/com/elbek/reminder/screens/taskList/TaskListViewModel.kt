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

    //TODO: check mutability of this var
    private var taskList: TaskList? = null
    private var taskListType: TaskType? = null
    private var allTasks: List<Task>? = null

    val taskListNameText = Text()
    val dateTimeText = Text()

    val addNewTaskButtonVisible = Visible()
    val taskListItems = DataList<TaskListItem>()
    val completedTaskListItems = DataList<TaskListItem>()

    val setTaskListNameFocusCommand = Command()
    val openTaskScreenCommand = TCommand<TaskLaunchArgs>()
    val openNewTaskScreenCommand = TCommand<NewTaskLaunchArgs>()
    val openSettingsBottomSheetCommand = TCommand<String?>()

    //TODO: refactor conditions, separate into scenarios
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

    fun onTaskClicked(taskId: String, type: TaskClickType) = when (type) {
        TaskClickType.TASK -> {
            openTaskScreenCommand.call(
                TaskLaunchArgs(
                    taskListId = taskList?.id ?: taskListInteractor.getTaskListIdByTask(taskId),
                    taskId = taskId
                )
            )
        }
        else -> {
            //TODO: refactor conditions
            val task = (taskList?.tasks?.find { taskId == it.id } ?: allTasks!!.find { taskId == it.id })!!.apply {
                when (type) {
                    TaskClickType.CHECKBOX -> isCompleted = !isCompleted
                    TaskClickType.IMPORTANT -> isImportant = !isImportant
                    else -> {
                        // no op
                    }
                }
            }
            val taskListId = taskListInteractor.getTaskListIdByTask(taskId) ?: defaultTaskListInteractor.getTaskListId()
            val isCustomTaskList = !defaultTaskListInteractor.isDefaultTaskList(taskListId)

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
            setCompleteTasks(it.tasks)
        } ?: run {
            taskListNameText.value = taskListType?.title ?: ""
            setTasks(allTasks)
            setCompleteTasks(allTasks)
        }

        dateTimeText.value = "Saturday, 21 November"
        addNewTaskButtonVisible.value = (taskListType != TaskType.COMPLETED || taskListType == null)
    }

    private fun setTasks(tasks: List<Task>?) = tasks?.let { items ->
        Observable.fromIterable(items)
            .filter { !it.isCompleted }
            .map {
                TaskListItem(
                    taskId = it.id,
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

    private fun setCompleteTasks(tasks: List<Task>?) = tasks?.let { items ->
        Observable.fromIterable(items)
            .filter { it.isCompleted }
            .map {
                TaskListItem(
                    taskId = it.id,
                    name = it.name,
                    taskCompleted = "",
                    isImportant = it.isImportant,
                    isCompleted = it.isCompleted
                )
            }.toList()
            .subscribeOnIoObserveOnMain { completedTaskListItems.value = it }
            .addToSubscriptions()
    } ?: run {
        //TODO: show some empty state
    }
}
