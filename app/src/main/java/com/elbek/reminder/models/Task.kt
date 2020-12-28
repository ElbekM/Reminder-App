package com.elbek.reminder.models

import com.elbek.reminder.database.entities.SubTaskEntity
import java.util.Date

data class Task(
    val id: Int,
    val name: String,
    val description: String? = null,
    val subTasks: List<SubTaskEntity>? = null,
    val createdDate: Date? = null,
    val date: Date? = null,
    val isImportant: Boolean = false,
    val isInMyDate: Boolean = false,
    val isCompleted: Boolean = false
)
