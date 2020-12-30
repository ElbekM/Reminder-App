package com.elbek.reminder.common.extensions

import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.elbek.reminder.common.core.Command
import com.elbek.reminder.common.core.Data
import com.elbek.reminder.common.core.TCommand
import com.elbek.reminder.common.core.Visible
import com.elbek.reminder.common.core.DataList
import com.elbek.reminder.common.core.Enabled
import com.elbek.reminder.common.core.Text
import com.elbek.reminder.common.core.properties.WrappedMutableLiveData

fun LifecycleOwner.bindCommand(command: Command, block: () -> Unit) =
    command.observe(this, { block.invoke() })

fun <T> LifecycleOwner.bindCommand(command: TCommand<T>, block: (T) -> Unit) =
    command.observe(this, Observer(block))

fun LifecycleOwner.bindVisible(liveData: Visible, view: View, setInvisible: Boolean = false) =
    liveData.observe(this, {
        if (setInvisible) view.isInvisible = it
        else view.isVisible = it
    })

fun LifecycleOwner.bindEnabled(liveData: Enabled, view: View) =
    liveData.observe(this, { view.isEnabled = it })

fun <T> LifecycleOwner.bindDataToAction(liveData: Data<T>, block: (T) -> Unit) =
    liveData.observe(this, Observer(block))

fun <T> LifecycleOwner.bindDataToAction(liveData: WrappedMutableLiveData<T>, block: (T) -> Unit) =
    liveData.observe(this, Observer(block))

fun <T> LifecycleOwner.bindDataToAction(liveData: DataList<T>, block: (List<T>) -> Unit) =
    liveData.observe(this, Observer(block))

fun LifecycleOwner.bindText(liveData: Text, textView: TextView) =
    liveData.observe(this, { textView.text = it })
