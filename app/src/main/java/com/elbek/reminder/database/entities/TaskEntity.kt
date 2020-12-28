package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.Task
import java.util.Date

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String?,
    val subTasks: List<SubTaskEntity>?,
    val createdDate: Date?,
    val date: Date?,
    val isImportant: Boolean,
    val isInMyDate: Boolean,
    val isCompleted: Boolean
) {
    fun toModel(): Task = Task(
        id = id,
        name = name,
        description = description,
        subTasks = subTasks,
        createdDate = createdDate,
        date = date,
        isImportant = isImportant,
        isInMyDate = isInMyDate,
        isCompleted = isCompleted
    )
}
