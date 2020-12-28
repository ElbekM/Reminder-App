package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.TaskList

@Entity
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val icon: Int?,
    val name: String,
    val tasks: List<TaskEntity>?
) {
    fun toModel(): TaskList = TaskList(
        id = id,
        icon = icon,
        name = name,
        tasks = tasks
    )
}
