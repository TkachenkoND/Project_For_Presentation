package com.online.bk.olimpbet.presentation.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.online.bk.olimpbet.R
import com.online.bk.olimpbet.data.helper.NO_INTERNET_CONNECT
import com.online.bk.olimpbet.databinding.ActivityMainBinding
import com.online.bk.olimpbet.presentation.view.dialog.CustomDialog
import com.online.bk.olimpbet.presentation.view.fragment.LoadingFragment
import com.online.bk.olimpbet.presentation.view_model.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val sharedVm by viewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemBar()
        setNightModeNo()
        checkInetConnect()
    }

    private fun checkInetConnect() {
        sharedVm.checkForInternet()

        sharedVm.isConnection.observe(this) { isConnection ->
            if (isConnection) {
                goToNextFragment(LoadingFragment())
            } else
                CustomDialog.instance(NO_INTERNET_CONNECT)
                    .show(supportFragmentManager, CustomDialog.TAG)
        }
    }

    //View
    private fun goToNextFragment(frag: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.containerFragment, frag)
        }
    }

    private fun setNightModeNo() {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun hideSystemBar() {
        WindowCompat.setDecorFitsSystemWindows(this.window, false)

        WindowInsetsControllerCompat(
            this.window,
            this.window.decorView.rootView
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            this.window.decorView.setOnSystemUiVisibilityChangeListener {
                if (it != 0 && View.SYSTEM_UI_FLAG_FULLSCREEN != 0) {
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }

                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        }
    }

}