package com.elbek.reminder.screens.taskList

import com.elbek.reminder.screens.general.TaskType
import java.io.Serializable

data class TaskListLaunchArgs(
    val taskListId: String? = null,
    val taskType: TaskType? = null
): Serializable
