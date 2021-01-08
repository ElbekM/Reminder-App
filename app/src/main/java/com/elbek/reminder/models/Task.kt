package com.elbek.reminder.models

import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String? = null,
    var subTasks: MutableList<SubTask> = mutableListOf(),
    var createdDate: Date? = null,
    var date: Date? = null,
    var isImportant: Boolean = false,
    var isInMyDate: Boolean = false,
    var isCompleted: Boolean = false
)
