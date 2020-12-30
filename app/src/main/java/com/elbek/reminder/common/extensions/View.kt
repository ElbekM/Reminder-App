package com.elbek.reminder.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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
