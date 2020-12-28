package com.elbek.reminder.models

import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val subTasks: List<SubTask>? = null,
    val createdDate: Date? = null,
    val date: Date? = null,
    val isImportant: Boolean = false,
    val isInMyDate: Boolean = false,
    val isCompleted: Boolean = false
)
