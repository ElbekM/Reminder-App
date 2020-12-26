package com.elbek.reminder.screens.general

//TODO: Add icons and strings into resources
enum class TaskType(val icon: Int, val title: String) {
    MY_DAY(0, "My Day"),
    IMPORTANT(0, "Important"),
    TASKS(0, "Tasks"),
    COMPLETED(0, "Completed");

    companion object {
        fun getByTitle(title: String): TaskType = values().first { it.title == title }
    }
}
