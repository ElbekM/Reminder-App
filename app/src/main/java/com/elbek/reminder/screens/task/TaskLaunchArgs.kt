package com.elbek.reminder.screens.task

import java.io.Serializable

data class TaskLaunchArgs(
    val taskListId: String?,
    val taskId: String
) : Serializable
