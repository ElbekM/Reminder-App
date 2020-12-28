package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.SubTask

@Entity
data class SubTaskEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isCompleted: Boolean
) {
    constructor(subtask: SubTask) : this(
        id = subtask.id,
        name = subtask.name,
        isCompleted = subtask.isCompleted
    )

    fun toModel(): SubTask = SubTask(id, name, isCompleted)

    companion object {
        fun createList(subtasks: List<SubTask>?): List<SubTaskEntity>? =
            subtasks?.map { SubTaskEntity(it) }
    }
}
