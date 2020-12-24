package com.elbek.reminder.common.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

//TODO: check commit
fun DialogFragment.showAllowingStateLoss(
    fm: FragmentManager = childFragmentManager,
    tag: String = this::class.java.name
) =
    fm.beginTransaction()
        .add(this, tag)
        .addToBackStack(tag)
        .commitAllowingStateLoss()
