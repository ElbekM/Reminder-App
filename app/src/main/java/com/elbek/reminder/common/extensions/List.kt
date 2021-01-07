package com.elbek.reminder.common.extensions

fun <T> MutableList<T>.update(newValue: T, predicate: (T) -> Boolean): Boolean {
    this.forEachIndexed { index, value ->
        if (predicate(value)) {
            this[index] = newValue
            return true
        }
    }
    return false
}
