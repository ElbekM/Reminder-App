package com.elbek.reminder.screens.taskList.newTask

import com.elbek.reminder.screens.general.TaskType
import java.io.Serializable

data class NewTaskLaunchArgs(
    val taskListId: String,
    val taskListType: TaskType? = null
): Serializable
