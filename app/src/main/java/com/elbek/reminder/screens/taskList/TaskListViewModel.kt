package com.elbek.reminder.screens.taskList

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel

class TaskListViewModel @ViewModelInject constructor(
    application: Application
) : BaseViewModel(application) {

    fun init(taskListId: String?) {

    }
}
