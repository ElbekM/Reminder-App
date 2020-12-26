package com.elbek.reminder.screens.general

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.DataList
import com.elbek.reminder.common.core.TCommand
import com.elbek.reminder.screens.general.adapters.TaskTypeItem

class GeneralViewModel @ViewModelInject constructor(
    application: Application
) : BaseViewModel(application) {

    val taskTypes = DataList<TaskTypeItem>()
    val openTaskListScreenCommand = TCommand<TaskType>()

    fun init() {
        //openTaskListScreenCommand.call(TaskType.MY_DAY)
        setupTaskTypes()
    }

    fun onTypeTaskClicked(position: Int) {
        TaskType.values().firstOrNull {
            it.title == taskTypes.value[position].title
        }?.let {
            openTaskListScreenCommand.call(it)
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
}
