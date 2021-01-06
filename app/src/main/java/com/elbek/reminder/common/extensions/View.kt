package com.elbek.reminder.common.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

internal fun View.hideKeyboard() {
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also { manager ->
        manager.takeIf { it.isActive }?.hideSoftInputFromWindow(windowToken, 0)
    }
}

internal fun View.showKeyboard() {
    this.requestFocus()
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also { manager ->
        manager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}
