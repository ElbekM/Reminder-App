package com.elbek.reminder.screens.taskList.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseBottomSheetFragment
import com.elbek.reminder.common.extensions.bindCommand
import com.elbek.reminder.common.extensions.bindEnabled
import com.elbek.reminder.common.extensions.castParent
import com.elbek.reminder.databinding.FragmentTasklistSettingsBinding
import com.elbek.reminder.screens.taskList.TaskListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListSettingsBottomSheetFragment : BaseBottomSheetFragment<TaskListSettingsViewModel>() {

    override val viewModel: TaskListSettingsViewModel by viewModels()
    override val binding: FragmentTasklistSettingsBinding by viewLifecycleAware {
        FragmentTasklistSettingsBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTasklistSettingsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(
            requireArguments().getString(taskListIdKey)
        )
    }

    override fun bindViewModel() {
        super.bindViewModel()

        with(binding) {
            bindEnabled(viewModel.renameButtonEnabled, renameButtonTextView)
            bindEnabled(viewModel.deleteButtonEnabled, deleteButtonTextView)

            bindCommand(viewModel.taskListRemoved) {
                castParent<TaskListFragment>()?.onTaskListDeleted()
                onBackPressed()
            }
        }
    }

    private fun initViews() = with(binding) {
        renameButtonTextView.setOnClickListener {
            castParent<TaskListFragment>()?.onTaskListRenamed()
            onBackPressed()
        }
        deleteButtonTextView.setOnClickListener {
            viewModel.onDeleteClicked()
        }
    }

    interface Listener {
        fun onTaskListRenamed()
        fun onTaskListDeleted()
    }

    companion object {
        private val taskListIdKey: String = ::taskListIdKey.name

        fun newInstance(taskListId: String? = null): TaskListSettingsBottomSheetFragment =
            TaskListSettingsBottomSheetFragment().apply {
                arguments = bundleOf(taskListIdKey to taskListId)
            }
    }
}
