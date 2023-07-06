package com.online.bk.olimpbet.presentation.view.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.online.bk.olimpbet.R
import com.online.bk.olimpbet.presentation.view_model.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var _viewBinding: B? = null
    protected val binding get() = checkNotNull(_viewBinding)

    protected val sharedVm by sharedViewModel<SharedViewModel>()

    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): B

    protected fun goToNextFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            replace(R.id.containerFragment, fragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        _viewBinding = initBinding(inflater, container)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}