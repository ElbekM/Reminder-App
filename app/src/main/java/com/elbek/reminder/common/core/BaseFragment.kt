package com.elbek.reminder.common.core

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.elbek.reminder.R
import com.elbek.reminder.common.extensions.bindCommand

//TODO: check DialogFragment, and orientation
abstract class BaseFragment<VM> : DialogFragment() where VM : BaseViewModel {

    private val originalScreenOrientationKey: String = ::originalScreenOrientationKey.name

    protected abstract val viewModel: VM
    protected abstract val binding: ViewBinding

    protected open var customTheme: Int = R.style.AppTheme
    protected open val screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, customTheme)
        isCancelable = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (arguments == null) {
            arguments = Bundle()
        }

        requireArguments().putInt(originalScreenOrientationKey, requireActivity().requestedOrientation)
    }

    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation = screenOrientation

        viewModel.start()
    }

    override fun onPause() {
        super.onPause()

        requireActivity().requestedOrientation = requireArguments().getInt(originalScreenOrientationKey)

        viewModel.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.destroy()
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                // Workaround to avoid multiple calls of KeyListener.
                // It prevents closing multiple fragments at one time.
                Handler(Looper.getMainLooper()).postDelayed({ onBackPressed() }, 100)
                return@OnKeyListener true

            } else {
                return@OnKeyListener false
            }
        })
    }

    protected open fun bindViewModel() = with(viewModel) {
        bindCommand(closeCommand) { onBackPressed() }
        bindCommand(requestPermissionsCommand) { (permissions, requestCode) ->
            requestPermissions(permissions.toTypedArray(), requestCode)
        }
        bindCommand(showPermissionDialogDeniedByUserCommand) { (permission, requestCode) ->
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                viewModel.permissionDeniedByUser(requestCode)
            }
        }
    }

    protected open fun onBackPressed() = dismissAllowingStateLoss()

    protected fun <T> Fragment.viewLifecycleAware(initialise: () -> T) =
        ViewBindingDelegate(this, initialise)
}
