package com.elbek.reminder.database

import androidx.room.TypeConverter
import com.elbek.reminder.database.entities.SubTaskEntity
import com.elbek.reminder.database.entities.TaskEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class TypeConverter {

    @TypeConverter
    fun fromTasksMutableList(tasks: MutableList<TaskEntity>?): String = Gson().toJson(tasks)

    @TypeConverter
    fun toTasksMutableList(data: String): MutableList<TaskEntity>? =
        Gson().fromJson(data, object : TypeToken<ArrayList<TaskEntity>>() {}.type)

    @TypeConverter
    fun fromSubTasksMutableList(tasks: MutableList<SubTaskEntity>?): String = Gson().toJson(tasks)

    @TypeConverter
    fun toSubTasksMutableList(data: String): MutableList<SubTaskEntity>? =
        Gson().fromJson(data, object : TypeToken<ArrayList<SubTaskEntity>>() {}.type)
}
