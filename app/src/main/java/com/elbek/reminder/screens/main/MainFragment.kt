package com.elbek.reminder.screens.main

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.elbek.reminder.common.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }
}
