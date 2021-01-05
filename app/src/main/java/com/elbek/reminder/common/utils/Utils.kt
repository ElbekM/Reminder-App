package com.elbek.reminder.common.utils

import com.elbek.reminder.models.TaskList

object Utils {

    fun validateTaskCount(count: Int): String = "$count tasks"

    fun List<TaskList>.findById(id: String) = find { it.id == id }
}
