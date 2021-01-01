package com.elbek.reminder.screens.taskList.task

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
            requireArguments().getString(taskListIdKey)!!
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
        private val taskListIdKey: String = ::taskListIdKey.name

        fun newInstance(taskListId: String): NewTaskBottomSheetFragment =
            NewTaskBottomSheetFragment().apply {
                arguments = bundleOf(taskListIdKey to taskListId)
            }
    }
}