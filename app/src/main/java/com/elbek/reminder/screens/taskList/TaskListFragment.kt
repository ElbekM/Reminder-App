package com.elbek.reminder.screens.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseFragment
import com.elbek.reminder.common.extensions.bindCommand
import com.elbek.reminder.common.extensions.bindDataToAction
import com.elbek.reminder.common.extensions.bindText
import com.elbek.reminder.common.extensions.bindVisible
import com.elbek.reminder.common.extensions.show
import com.elbek.reminder.databinding.FragmentTasklistBinding
import com.elbek.reminder.screens.taskList.adapter.TaskListAdapter
import com.elbek.reminder.screens.taskList.settings.TaskListSettingsBottomSheetFragment
import com.elbek.reminder.views.TaskEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : BaseFragment<TaskListViewModel>(), TaskListSettingsBottomSheetFragment.Listener {

    override val viewModel: TaskListViewModel by viewModels()
    override val binding: FragmentTasklistBinding by viewLifecycleAware {
        FragmentTasklistBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTasklistBinding.inflate(inflater, container, false).root

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
            bindText(viewModel.taskListNameText, taskListNameEditText)
            bindText(viewModel.dateTimeText, dateTextView)
            bindVisible(viewModel.addNewTaskButtonVisible, addNewTaskCardView)

            bindCommand(viewModel.openNewTaskScreenCommand) {
                //TODO: add new task via bottom sheet (NO, like new TaskList)
            }
            bindCommand(viewModel.openSettingsBottomSheetCommand) {
                TaskListSettingsBottomSheetFragment
                    .newInstance(it)
                    .show(childFragmentManager)
            }
            bindCommand(viewModel.setTaskListNameFocusCommand) {
                taskListNameEditText.enableEditor()
            }

            bindDataToAction(viewModel.taskListItems) { items ->
                var adapter = taskListRecyclerView.adapter as? TaskListAdapter
                if (adapter == null) {
                    adapter = TaskListAdapter { (position, clickType) ->
                        viewModel.onTaskClicked(position, clickType)
                    }
                    taskListRecyclerView.adapter = adapter
                }
                adapter.setItems(items)
            }
        }
    }

    override fun onTaskListRenamed() {
        viewModel.setFocusToTitle()
    }

    override fun onTaskListDeleted() = onBackPressed()

    private fun initViews() = with(binding) {
        backImageView.setOnClickListener { onBackPressed() }
        settingsImageView.setOnClickListener { viewModel.onSettingsClicked() }
        addNewTaskCardView.setOnClickListener { viewModel.onAddNewTaskClicked() }

        taskListNameEditText.apply {
            initKeyListener()
            setOnKeyListener(object : TaskEditText.KeyListener {
                override fun onComplete() {
                    viewModel.onTaskListNameUpdated(text.toString())
                }
            })
        }
    }

    companion object {
        private val taskListIdKey: String = ::taskListIdKey.name

        fun newInstance(taskListId: String? = null): TaskListFragment =
            TaskListFragment().apply {
                arguments = bundleOf(taskListIdKey to taskListId)
            }
    }
}
