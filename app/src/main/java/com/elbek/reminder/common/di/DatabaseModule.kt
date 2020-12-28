package com.elbek.reminder.common.di

import android.content.Context
import androidx.room.Room
import com.elbek.reminder.database.AppDatabase
import com.elbek.reminder.database.TaskListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "reminder.db"
        ).build()

    @Provides
    fun provideTaskListDao(appDatabase: AppDatabase): TaskListDao =
        appDatabase.getTaskListDao()
}
