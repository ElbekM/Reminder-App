package com.elbek.reminder.screens.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseFragment
import com.elbek.reminder.databinding.FragmentTasklistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : BaseFragment<TaskListViewModel>() {

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
        viewModel.init()
    }

    override fun bindViewModel() {
        super.bindViewModel()
    }

    private fun initViews() {

    }

    companion object {
        fun newInstance(): TaskListFragment = TaskListFragment()
    }
}
