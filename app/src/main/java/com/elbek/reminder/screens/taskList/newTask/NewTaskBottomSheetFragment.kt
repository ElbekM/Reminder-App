package com.elbek.reminder.screens.taskList.newTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseBottomSheetFragment
import com.elbek.reminder.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskBottomSheetFragment : BaseBottomSheetFragment<NewTaskViewModel>() {

    override val viewModel: NewTaskViewModel by viewModels()
    override val binding: FragmentNewTaskBinding by viewLifecycleAware {
        FragmentNewTaskBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNewTaskBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(
            requireArguments().getSerializable(newTaskArgsKey) as NewTaskLaunchArgs
        )
    }

    override fun bindViewModel() {
        super.bindViewModel()
    }

    private fun initViews() = with(binding) {
        addTaskImageView.setOnClickListener {
            viewModel.onAddTaskClicked(taskNameEditText.text.toString())
        }
    }

    companion object {
        private val newTaskArgsKey: String = ::newTaskArgsKey.name

        fun newInstance(args: NewTaskLaunchArgs): NewTaskBottomSheetFragment =
            NewTaskBottomSheetFragment().apply {
                arguments = bundleOf(newTaskArgsKey to args)
            }
    }
}
