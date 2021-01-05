package com.elbek.reminder.screens.general.adapters

import com.elbek.reminder.screens.general.TaskType

data class TaskTypeItem(
    val icon: Int?,
    val title: String?,
    val taskCount: Int,
    val type: TaskType
)
