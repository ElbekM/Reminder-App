package com.elbek.reminder.screens.task.newSubTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseBottomSheetFragment
import com.elbek.reminder.databinding.FragmentNewSubtaskBinding
import com.elbek.reminder.screens.task.TaskLaunchArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewSubTaskBottomSheetFragment : BaseBottomSheetFragment<NewSubTaskViewModel>() {

    override val viewModel: NewSubTaskViewModel by viewModels()
    override val binding: FragmentNewSubtaskBinding by viewLifecycleAware {
        FragmentNewSubtaskBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewSubtaskBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(
            requireArguments().getSerializable(newSubTaskArgsKey) as TaskLaunchArgs
        )
    }

    override fun bindViewModel() {
        super.bindViewModel()
    }

    private fun initViews() = with(binding) {
        addSubTaskImageView.setOnClickListener {
            viewModel.onAddSubTaskClicked(subTaskNameEditText.text.toString())
        }
    }

    companion object {
        private val newSubTaskArgsKey: String = ::newSubTaskArgsKey.name

        fun newInstance(args: TaskLaunchArgs): NewSubTaskBottomSheetFragment =
            NewSubTaskBottomSheetFragment().apply {
                arguments = bundleOf(newSubTaskArgsKey to args)
            }
    }
}
