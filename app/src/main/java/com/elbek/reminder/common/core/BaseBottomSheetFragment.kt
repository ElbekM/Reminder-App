package com.elbek.reminder.common.core

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.elbek.reminder.R
import com.elbek.reminder.common.extensions.bindCommand
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VM> : BottomSheetDialogFragment() where VM : BaseViewModel {

    protected abstract val viewModel: VM
    protected abstract val binding: ViewBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireView().clipToOutline = true

        requireDialog().setOnShowListener {
            val dialog = requireDialog() as BottomSheetDialog
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            coordinatorLayout = bottomSheet?.parent as CoordinatorLayout
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = bottomSheet.height
            coordinatorLayout.parent.requestLayout()
        }
    }

    protected open fun bindViewModel() {
        bindCommand(viewModel.closeCommand) { onBackPressed() }
    }

    protected open fun onBackPressed() = dismissAllowingStateLoss()

    fun updateBottomSheetPickHeight(view: View) {
        ObjectAnimator.ofInt(
            bottomSheetBehavior, "peekHeight",
            bottomSheetBehavior.peekHeight, bottomSheetBehavior.peekHeight + view.height
        )
            .apply {
                duration = 250
                start()
            }
    }
}