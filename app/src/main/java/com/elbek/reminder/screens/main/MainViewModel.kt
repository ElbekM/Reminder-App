package com.elbek.reminder.screens.main

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel
import com.elbek.reminder.common.core.TCommand

class MainViewModel @ViewModelInject constructor(
    application: Application
) : BaseViewModel(application) {

    val switchScreenCommand = TCommand<MainTab>()

    fun init() {

    }

    fun onBottomNavigationClicked(index: Int) {
        val tab = MainTab.getByIndex(index)
        switchScreenCommand.call(tab)
    }
}
