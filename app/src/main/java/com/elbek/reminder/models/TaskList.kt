package com.elbek.reminder.models

import com.elbek.reminder.database.entities.TaskEntity

data class TaskList(
    val id: Int,
    val icon: Int? = null,
    val name: String,
    val tasks: List<TaskEntity>? = null
)
