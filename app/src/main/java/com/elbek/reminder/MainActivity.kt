package com.elbek.reminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.elbek.reminder.common.extensions.showAllowingStateLoss
import com.elbek.reminder.screens.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).apply { fitsSystemWindows = true })

        launchMainScreen()
    }

    private fun launchMainScreen() {
        MainFragment
            .newInstance()
            .showAllowingStateLoss(supportFragmentManager)
    }
}
