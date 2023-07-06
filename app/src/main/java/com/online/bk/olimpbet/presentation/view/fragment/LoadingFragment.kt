package com.online.bk.olimpbet.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.onesignal.OneSignal
import com.online.bk.olimpbet.data.helper.ONESIGNAL_APP_KEY
import com.online.bk.olimpbet.databinding.LoadingFragmentBinding

class LoadingFragment : BaseFragment<LoadingFragmentBinding>() {

    private var fullLink: String? = null
    private var startLink = "null"

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        LoadingFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserveSaveFlagZaglushka()
    }

    //OneSignal Init
    private fun initOneSignal() {
        OneSignal.initWithContext(requireContext())
        OneSignal.setAppId(ONESIGNAL_APP_KEY)
    }

    private fun initOneSignalNotificationOpenedHandler() {
        OneSignal.setNotificationOpenedHandler {
            val stringNotification = it.notification.launchURL
            if (stringNotification != null) {
                sharedVm.saveStringNotificationOneSignal(stringNotification)
            }
        }
    }

    private fun initObserveSaveFlagZaglushka() {

        val oneSignalUrl = sharedVm.getStringNotification().toString()

        if (oneSignalUrl.isNotEmpty()) {
            sharedVm.setFullLink(oneSignalUrl)
            goToNextFragment(mnbvcerteyru67687tre5wghe6jr75kERTRYUIudysg())
        } else {
            if (sharedVm.fetchFlagZaglushkaFromSharedVm().isNullOrEmpty())
                initObserveSaveLink()
            else
                goToNextFragment(MenuGameFragment())
        }
    }

    private fun initObserveSaveLink() {
        if (sharedVm.fetchFlagLinkFromSharedVm().isNullOrEmpty())
            initObserveEmulator()
        else
            goToNextFragment(mnbvcerteyru67687tre5wghe6jr75kERTRYUIudysg())
    }

    private fun initObserveEmulator() {
        if (sharedVm.isEmulator())
            goToNextFragment(MenuGameFragment())
        else {
            initObserveDataFromJson()
        }
    }

    private fun initObserveDataFromJson() {
        initOneSignal()
        initOneSignalNotificationOpenedHandler()

        sharedVm.fetchDataByLocation()
        observeLinkByLocation()
    }

    private fun observeLinkByLocation() {
        sharedVm.linkTm.observe(viewLifecycleOwner) { linkTm ->
            if (!linkTm.isNullOrEmpty() && linkTm != "null") {
                startLink = linkTm
                sharedVm.setAfIdOrganic()
                sharedVm.saveFlagLinkInSharedVm()

                addOptionsGoToSite()
            } else
                goToNextFragment(MenuGameFragment())
        }
    }

    private fun addOptionsGoToSite() {
        if (startLink.isNotEmpty()) {

            OneSignal.setExternalUserId(sharedVm.afId.value.toString())

            if (startLink.last() == '&')
                startLink = startLink.substring(0, startLink.length - 1)

            fullLink = concatName(
                startLink = startLink,
                afId = sharedVm.afId.value.toString()
            )

            sharedVm.saveFullLinkInShared(fullLink)
            sharedVm.setFullLink(fullLink.toString())

            goToNextFragment(mnbvcerteyru67687tre5wghe6jr75kERTRYUIudysg())
        } else
            goToNextFragment(MenuGameFragment())
    }

    private fun concatName(
        packageName: String = requireContext().packageName,
        startLink: String,
        afId: String? = null
    ) = startLink.toUri().buildUpon().apply {

        if (afId.isNullOrEmpty()) {
            appendQueryParameter("bn", "$packageName")
            appendQueryParameter("afid", "null")
        } else {
            appendQueryParameter("bn", "$packageName")
            appendQueryParameter("afid", "$afId")
        }

    }.toString()

}