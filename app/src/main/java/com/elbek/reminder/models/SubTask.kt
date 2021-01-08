package com.elbek.reminder.models

import java.util.UUID

data class SubTask(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var isCompleted: Boolean = false
)
