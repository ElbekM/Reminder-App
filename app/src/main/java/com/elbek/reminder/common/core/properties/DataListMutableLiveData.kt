package com.elbek.reminder.common.core.properties

class DataListMutableLiveData<T>(defaultValue: List<T> = emptyList()) :
    WrappedMutableLiveData<List<T>>(defaultValue)
