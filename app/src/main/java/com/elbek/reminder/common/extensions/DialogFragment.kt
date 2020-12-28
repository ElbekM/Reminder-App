package com.elbek.reminder.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

//TODO: check commit, set fm by default
fun Fragment.showAllowingStateLoss(
    fm: FragmentManager,
    tag: String = this::class.java.name
) =
    fm.beginTransaction()
        .add(this, tag)
        .addToBackStack(tag)
        .commitAllowingStateLoss()
