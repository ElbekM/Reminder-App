package com.elbek.reminder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elbek.reminder.database.entities.TaskListEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TaskListDao {
    @Query("SELECT * FROM tasklistentity")
    fun getAllTaskLists(): Single<List<TaskListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskList: List<TaskListEntity>): Completable
}
