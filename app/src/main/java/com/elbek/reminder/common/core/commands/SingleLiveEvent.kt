package com.elbek.reminder.common.core.commands

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : (T) -> Unit {

    private val liveData = SingleMutableLiveData<T>()

    override fun invoke(arg: T) = call(arg)

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) = liveData.observe(owner, observer)

    @MainThread
    fun call(value: T) {
        liveData.value = value
    }
}
