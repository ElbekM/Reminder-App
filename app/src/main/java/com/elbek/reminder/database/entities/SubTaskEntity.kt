package com.elbek.reminder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elbek.reminder.models.SubTask

@Entity
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val isCompleted: Boolean
) {
    fun toModel(): SubTask = SubTask(id, name, isCompleted)
}
