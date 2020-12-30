package com.elbek.reminder.models

import java.util.UUID

data class TaskList(
    val id: String = UUID.randomUUID().toString(),
    val icon: Int? = null,
    var name: String?,
    val tasks: List<Task>? = null
)
