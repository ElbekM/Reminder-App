package com.elbek.reminder.common.core

import com.elbek.reminder.common.core.commands.LiveEvent
import com.elbek.reminder.common.core.commands.SingleLiveEvent
import com.elbek.reminder.common.core.properties.*

typealias Command = LiveEvent
typealias TCommand<T> = SingleLiveEvent<T>
typealias Data<T> = DataMutableLiveData<T>
typealias Text = TextMutableLiveData
typealias Visible = VisibleMutableLiveData
typealias Progress = ProgressMutableLiveData
typealias DataList<T> = DataListMutableLiveData<T>
