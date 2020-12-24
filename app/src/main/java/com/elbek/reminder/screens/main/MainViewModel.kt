package com.elbek.reminder.screens.main

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.elbek.reminder.common.core.BaseViewModel

class MainViewModel @ViewModelInject constructor(
    application: Application
) : BaseViewModel(application) {

    fun init() {
    }
}
