package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.Task
import java.util.Date

@Entity
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isImportant: Boolean,
    val isInMyDate: Boolean,
    val isCompleted: Boolean,
    val description: String?,
    val subTasks: List<SubTaskEntity>?,
    val createdDate: Date?,
    val date: Date?
) {
    constructor(task: Task) : this(
        id = task.id,
        name = task.name,
        isImportant = task.isImportant,
        isInMyDate = task.isInMyDate,
        isCompleted = task.isCompleted,
        description = task.description,
        subTasks = SubTaskEntity.createList(task.subTasks),
        createdDate = task.createdDate,
        date = task.date
    )

    fun toModel(): Task = Task(
        id = id,
        name = name,
        description = description,
        subTasks = subTasks?.map { it.toModel() }?.toMutableList() ?: mutableListOf(),
        createdDate = createdDate,
        date = date,
        isImportant = isImportant,
        isInMyDate = isInMyDate,
        isCompleted = isCompleted
    )

    companion object {
        fun createList(taskLists: List<Task>?): List<TaskEntity>? =
            taskLists?.map { TaskEntity(it) }
    }
}
