package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.TaskList

@Entity
data class TaskListEntity(
    @PrimaryKey
    val id: String,
    val icon: Int?,
    val name: String?,
    val tasks: List<TaskEntity>?
) {
    constructor(taskLists: TaskList) : this(
        id = taskLists.id,
        icon = taskLists.icon,
        name = taskLists.name,
        tasks = TaskEntity.createList(taskLists.tasks)
    )

    fun toModel(): TaskList = TaskList(
        id = id,
        icon = icon,
        name = name,
        tasks = tasks?.map { it.toModel() }
    )

    companion object {
        fun createList(taskLists: List<TaskList>): List<TaskListEntity> =
            taskLists.map { TaskListEntity(it) }
    }
}
