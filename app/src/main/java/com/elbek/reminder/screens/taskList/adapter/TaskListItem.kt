package com.elbek.reminder.screens.taskList.adapter

data class TaskListItem(
    val name: String,
    val taskCompleted: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false
)
