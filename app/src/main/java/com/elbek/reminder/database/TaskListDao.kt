package com.elbek.reminder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elbek.reminder.database.entities.TaskEntity
import com.elbek.reminder.database.entities.TaskListEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TaskListDao {
    @Query("SELECT * FROM tasklistentity")
    fun getAllTaskLists(): Single<List<TaskListEntity>>

    @Query("SELECT * FROM tasklistentity WHERE id = :id")
    fun getTaskList(id: String): Single<TaskListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskList: TaskListEntity): Completable

    @Insert
    fun insert(taskLists: List<TaskListEntity>): Completable

    @Query("DELETE FROM tasklistentity WHERE id = :id")
    fun deleteTaskList(id: String): Completable

    @Query("UPDATE tasklistentity SET name = :name WHERE id = :id")
    fun updateTaskListName(id: String, name: String): Completable

    @Query("UPDATE tasklistentity SET tasks = :tasks WHERE id = :id")
    fun updateTasks(id: String, tasks: List<TaskEntity>): Completable
}
