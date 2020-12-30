package com.elbek.reminder.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

val Fragment.parent: Any?
    get() = parentFragment ?: activity

//TODO: check commit, set fm by default
fun Fragment.showAllowingStateLoss(
    fm: FragmentManager,
    tag: String = this::class.java.name
) =
    fm.beginTransaction()
        .add(this, tag)
        .addToBackStack(tag)
        .commitAllowingStateLoss()

fun BottomSheetDialogFragment.show(fm: FragmentManager) =
    this.show(fm, this::class.java.name)

inline fun <reified T> Fragment.castParent(): T? = parent as? T
