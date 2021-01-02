package com.elbek.reminder.common.extensions

fun <T> List<T>.update(newValue: T, condition: (T) -> Boolean): List<T> =
    map { if (condition(it)) newValue else it }
