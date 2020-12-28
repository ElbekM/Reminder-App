package com.elbek.reminder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elbek.reminder.database.entities.TaskListEntity

@Database(entities = [TaskListEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTaskListDao(): TaskListDao
}
