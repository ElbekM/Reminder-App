package com.elbek.reminder.screens.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.elbek.reminder.R
import com.elbek.reminder.common.core.BaseFragment
import com.elbek.reminder.common.extensions.bindCommand
import com.elbek.reminder.common.extensions.bindDataToAction
import com.elbek.reminder.common.extensions.bindText
import com.elbek.reminder.common.extensions.setStrikeFlag
import com.elbek.reminder.common.extensions.setTint
import com.elbek.reminder.common.extensions.show
import com.elbek.reminder.databinding.FragmentTaskBinding
import com.elbek.reminder.screens.task.adapter.SubTaskListAdapter
import com.elbek.reminder.screens.task.newSubTask.NewSubTaskBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : BaseFragment<TaskViewModel>() {

    override val viewModel: TaskViewModel by viewModels()
    override val binding: FragmentTaskBinding by viewLifecycleAware {
        FragmentTaskBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTaskBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(
            requireArguments().getSerializable(taskArgsKey) as TaskLaunchArgs
        )
    }

    override fun bindViewModel() {
        super.bindViewModel()

        with(binding) {
            bindText(viewModel.toolbarTitleText, toolbarTitleTextView)
            bindText(viewModel.taskNameText, taskNameEditText)
            bindText(viewModel.taskNotesText, taskNotesEditText)

            bindCommand(viewModel.openNewSubTaskScreenCommand) {
                NewSubTaskBottomSheetFragment
                    .newInstance(it)
                    .show(childFragmentManager)
            }

            bindDataToAction(viewModel.isTaskCompleted) { completed ->
                taskCheckbox.isChecked = completed
                taskNameEditText.setStrikeFlag(completed)
            }
            bindDataToAction(viewModel.isImportant) { important ->
                importantImageView.apply {
                    if (important) setTint(R.color.colorPrimary)
                    else setTint(R.color.colorPrimaryDark)
                }
            }
            bindDataToAction(viewModel.isInMyDay) { inMyDay ->
                addToMyDayImageView.apply {
                    if (inMyDay) setTint(R.color.colorPrimary)
                    else setTint(R.color.colorPrimaryDark)
                }
            }
            bindDataToAction(viewModel.subTaskItems) { items ->
                var adapter = subTaskRecyclerView.adapter as? SubTaskListAdapter
                if (adapter == null) {
                    adapter = SubTaskListAdapter { (position, clickType) ->
                        viewModel.onSubTaskClicked(position, clickType)
                    }
                    subTaskRecyclerView.adapter = adapter
                }
                adapter.setItems(items)
            }
        }
    }

    private fun initViews() = with(binding) {
        backImageView.setOnClickListener { onBackPressed() }
        deleteImageView.setOnClickListener { viewModel.onDeleteTaskClicked() }
        taskCheckbox.setOnClickListener { viewModel.onTaskCheckboxClicked() }
        importantImageView.setOnClickListener { viewModel.onImportantClicked() }
        addSubTaskLayout.setOnClickListener { viewModel.onAddNewSubTaskClicked() }
        myDayLayout.setOnClickListener { viewModel.onAddToMyDayClicked() }
        //dateLayout.setOnClickListener { viewModel }
        //fileLayout.setOnClickListener { viewModel }

        completeActionButton.setOnClickListener {
            viewModel.onCompleteClicked(taskNameEditText.text.toString(), taskNotesEditText.text.toString())
        }
    }

    companion object {
        private val taskArgsKey: String = ::taskArgsKey.name

        fun newInstance(args: TaskLaunchArgs): TaskFragment =
            TaskFragment().apply {
                arguments = bundleOf(taskArgsKey to args)
            }
    }
}
