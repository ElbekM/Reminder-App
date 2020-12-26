package com.elbek.reminder.screens.general.adapters

data class TaskCardItem(
    val icon: Int? = null,
    val title: String? = null,
    val taskCount: Int? = null,
    val progress: Int? = null,
    val cardType: TaskCardType = TaskCardType.TASK_LIST
)
