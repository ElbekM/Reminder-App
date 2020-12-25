package com.elbek.reminder.common.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T>(
    fragment: Fragment,
    private val initialise: () -> T
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null
    private var viewLifecycleOwner: LifecycleOwner? = null

    private var defaultLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
        }
    }

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { newLifecycleOwner ->
            viewLifecycleOwner?.lifecycle?.removeObserver(defaultLifecycleObserver)

            viewLifecycleOwner = newLifecycleOwner.also {
                it.lifecycle.addObserver(defaultLifecycleObserver)
            }
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        binding ?: initialise().also { binding = it }
}
