package com.elbek.reminder.screens.main

enum class MainTab(val index: Int) {
    GENERAL(0),
    PRODUCTIVITY(1),
    PROFILE(2);

    companion object {
        fun getByIndex(index: Int): MainTab = values().first { it.index == index }
    }
}
