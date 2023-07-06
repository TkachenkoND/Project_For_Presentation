package com.online.bk.olimpbet.presentation.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.online.bk.olimpbet.R
import com.online.bk.olimpbet.data.helper.EXIT
import com.online.bk.olimpbet.data.helper.NO_INTERNET_CONNECT
import com.online.bk.olimpbet.presentation.view_model.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CustomDialog : DialogFragment() {
    private lateinit var dialogFlag: String

    companion object {
        const val TAG = "CustomDialog"

        fun instance(dialogFlag: String): CustomDialog {
            val data = Bundle()
            data.putString("dialog_flag", dialogFlag)
            return CustomDialog().apply {
                arguments = data
            }
        }
    }

    private val sharedVm by sharedViewModel<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogFlag = arguments?.getString("dialog_flag").toString()

        return when (dialogFlag) {
            NO_INTERNET_CONNECT -> setCustomDialog("Something went wrong", "Repeat ?")
            EXIT -> setCustomDialog("", "Are you sure you want to exit?")
            else -> setCustomDialog("", "")
        }
    }

    private fun setCustomDialog(titleText: String, bodyText: String) =
        AlertDialog.Builder(requireContext())
            .setTitle(titleText)
            .setMessage(bodyText)
            .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                when (dialogFlag) {
                    NO_INTERNET_CONNECT -> sharedVm.checkForInternet()
                    EXIT -> requireActivity().finishAffinity()
                }
            }
            .setNegativeButton(getString(R.string.btn_no)) { _, _ ->
                when (dialogFlag) {
                    NO_INTERNET_CONNECT -> requireActivity().finishAffinity()
                }
            }
            .create()

}