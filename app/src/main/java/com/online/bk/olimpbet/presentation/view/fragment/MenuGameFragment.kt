package com.online.bk.olimpbet.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.online.bk.olimpbet.data.helper.POLICY_PRIVATE_URL
import com.online.bk.olimpbet.databinding.MenuGameFragmentBinding

class MenuGameFragment : BaseFragment<MenuGameFragmentBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        MenuGameFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedVm.saveFlagZaglushkaInSharedVm()
        initBtnClickListener()
    }

    private fun initBtnClickListener() {
        binding.apply {
            btnPlay.setOnClickListener {
                goToNextFragment(GameFragment())
            }

            btnInfo.setOnClickListener {
                Toast.makeText(requireContext(), "BEST FOOTBALL GAME", Toast.LENGTH_SHORT).show()
            }

            btnPolicyPrivate.setOnClickListener {
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(POLICY_PRIVATE_URL)
                )
                startActivity(i)
            }
        }
    }

}