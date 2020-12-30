package com.elbek.reminder.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.elbek.reminder.common.extensions.hideKeyboard

class TaskNameEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var listener: KeyListener? = null

    init {
        background = null
        inputType = EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isFocusable = false
            isFocusableInTouchMode = true

            listener?.onComplete()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    fun initKeyListener() {
        setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                isFocusable = false
                isFocusableInTouchMode = true

                listener?.onComplete()
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    fun setOnKeyListener(listener: KeyListener) {
        this.listener = listener
    }

    interface KeyListener {
        fun onComplete()
    }
}
