package com.elbek.reminder.screens.main

import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.commands.TCommand
import com.elbek.reminder.common.utils.Constants
import com.elbek.reminder.interactors.DefaultTaskListInteractor
import com.elbek.reminder.models.TaskList
import com.elbek.reminder.screens.general.TaskType
import io.reactivex.Observable
import io.reactivex.Single

class MainViewModel @ViewModelInject constructor(
    private val defaultTaskListInteractor: DefaultTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    val switchScreenCommand = TCommand<MainTab>()

    fun init() {
        checkAppFirstLaunch()
        switchScreenCommand.call(MainTab.GENERAL)
    }

    fun onBottomNavigationClicked(index: Int) {
        val tab = MainTab.getByIndex(index)
        switchScreenCommand.call(tab)
    }

    private fun checkAppFirstLaunch() {
        context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE).let { prefs ->
            if (prefs.getBoolean(Constants.APP_PREFERENCES_FIRST_INIT, true)) {
                prefs.edit().putBoolean(Constants.APP_PREFERENCES_FIRST_INIT, false).apply()
                initDefaultTaskLists()
            }
        }
    }

    private fun initDefaultTaskLists() {
        Single.just(TaskType.values())
            .map { it.toList() }
            .flatMap {
                Observable.fromIterable(it)
                    .map { taskType ->
                        TaskList(
                            icon = taskType.icon,
                            name = taskType.title
                        )
                    }.toList()
            }
            .flatMapCompletable { defaultTaskListInteractor.insertTaskLists(it) }
            .subscribeOnIoObserveOnMain()
            .addToSubscriptions()
    }
}
