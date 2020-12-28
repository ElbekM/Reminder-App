package com.elbek.reminder.models

data class SubTask(
    val id: Int,
    val name: String,
    val isCompleted: Boolean = false
)
