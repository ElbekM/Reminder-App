package com.elbek.reminder.common.core.properties

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlin.properties.Delegates

abstract class WrappedMutableLiveData<T>(defaultValue: T) {
    private val liveData = MutableLiveData<T>()

    var value by Delegates.observable<T>(defaultValue) { _, _, value -> liveData.value = value }

    init {
        value = defaultValue
    }

    fun observe(owner: LifecycleOwner, observer: Observer<T>) = liveData.observe(owner, observer)
    fun observeForever(observer: (value: T) -> Unit) = liveData.observeForever { observer(it) }
}
