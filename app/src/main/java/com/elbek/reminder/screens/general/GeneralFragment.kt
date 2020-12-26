package com.elbek.reminder.screens.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseFragment
import com.elbek.reminder.common.extensions.bindCommand
import com.elbek.reminder.common.extensions.bindDataToAction
import com.elbek.reminder.databinding.FragmentGeneralBinding
import com.elbek.reminder.screens.general.adapters.TaskTypeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralFragment : BaseFragment<GeneralViewModel>() {

    override val viewModel: GeneralViewModel by viewModels()
    override val binding: FragmentGeneralBinding by viewLifecycleAware {
        FragmentGeneralBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentGeneralBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        with(binding) {
            bindCommand(viewModel.openTaskListScreenCommand) { type ->
                //TODO: open tasklist screen
            }

            bindDataToAction(viewModel.taskTypes) {
                var adapter = taskTypeRecyclerView.adapter as? TaskTypeAdapter
                if (adapter == null) {
                    adapter = TaskTypeAdapter(viewModel::onTypeTaskClicked)
                    taskTypeRecyclerView.adapter = adapter
                }
                adapter.setItems(it)
            }
        }
    }

    private fun initViews() {

    }

    companion object {
        fun newInstance(): GeneralFragment = GeneralFragment()
    }
}