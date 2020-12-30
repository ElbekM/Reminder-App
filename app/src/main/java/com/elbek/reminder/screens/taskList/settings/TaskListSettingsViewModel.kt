package com.elbek.reminder.screens.taskList.settings

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.Command
import com.elbek.reminder.common.core.Enabled
import com.elbek.reminder.interactors.CustomTaskListInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskListSettingsViewModel @ViewModelInject constructor(
    private val taskListInteractor: CustomTaskListInteractor,
    application: Application
) : BaseViewModel(application) {

    private lateinit var taskListId: String

    val renameButtonEnabled = Enabled()
    val deleteButtonEnabled = Enabled()
    val taskListRemoved = Command()

    fun init(taskListId: String) {
        this.taskListId = taskListId

        val isCustomTaskList = taskListInteractor.getTaskListById(taskListId)?.run { true } ?: false
        renameButtonEnabled.value = isCustomTaskList
        deleteButtonEnabled.value = isCustomTaskList
    }

    fun onDeleteClicked() {
        taskListInteractor.deleteTaskListById(taskListId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                taskListRemoved.call()
            }, { logError(it) })
            .addToSubscriptions()
    }
}
