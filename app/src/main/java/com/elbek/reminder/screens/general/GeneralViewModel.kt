package com.elbek.reminder.screens.general

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.Command
import com.elbek.reminder.common.core.DataList
import com.elbek.reminder.common.core.TCommand
import com.elbek.reminder.screens.general.adapters.TaskCardItem
import com.elbek.reminder.screens.general.adapters.TaskCardType
import com.elbek.reminder.screens.general.adapters.TaskTypeItem

class GeneralViewModel @ViewModelInject constructor(
    application: Application
) : BaseViewModel(application) {

    val taskTypes = DataList<TaskTypeItem>()
    val taskCards = DataList<TaskCardItem>()

    val openTaskListByCardScreenCommand = Command()
    val openTaskListByTypeScreenCommand = TCommand<TaskType>()

    fun init() {
        //openTaskListScreenCommand.call(TaskType.MY_DAY)
        setupTaskTypes()
        setupTaskCards()
    }

    fun onTaskTypeClicked(position: Int) {
        TaskType.getByTitle(taskTypes.value[position].title).let { type ->
            openTaskListByTypeScreenCommand.call(type)
        }
    }

    fun onTaskCardClicked(cardType: TaskCardType, position: Int) {
        when (cardType) {
            TaskCardType.TASK_LIST -> {
                openTaskListByCardScreenCommand.call()
            }
            TaskCardType.ADD -> {
                //TODO: Add new task list
            }
        }
    }

    private fun setupTaskTypes() {
        //TODO: add task count
        val taskTypeList = mutableListOf<TaskTypeItem>()

        TaskType.values().forEach { taskType ->
            taskTypeList.add(
                TaskTypeItem(
                    taskType.icon,
                    taskType.title,
                    0
                )
            )
        }
        taskTypes.value = taskTypeList
    }

    private fun setupTaskCards() {
        //TODO: get data from database
        taskCards.value = listOf(
            TaskCardItem(0, "My Tasks", 8, 50),
            TaskCardItem(cardType = TaskCardType.ADD)
        )
    }
}
