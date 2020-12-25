package com.elbek.reminder.screens.productivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseFragment
import com.elbek.reminder.databinding.FragmentProductivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductivityFragment : BaseFragment<ProductivityViewModel>() {

    override val viewModel: ProductivityViewModel by viewModels()
    override val binding: FragmentProductivityBinding by viewLifecycleAware {
        FragmentProductivityBinding.bind(requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProductivityBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init()
    }

    private fun initViews() {

    }

    companion object {
        fun newInstance(): ProductivityFragment = ProductivityFragment()
    }
}
