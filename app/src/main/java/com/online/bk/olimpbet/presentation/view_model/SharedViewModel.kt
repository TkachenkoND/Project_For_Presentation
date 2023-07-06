package com.online.bk.olimpbet.presentation.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.online.bk.olimpbet.data.shared_preferences.WorkWithSharedPref
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

class SharedViewModel(
    private val context: Context,
    private val workWithSharedPref: WorkWithSharedPref,
    private val database: DatabaseReference
) : ViewModel() {

    private val _isConnection = MutableLiveData<Boolean>()
    val isConnection: LiveData<Boolean> = _isConnection

    private val _fullLink = MutableLiveData<String?>(null)
    val fullLink: LiveData<String?> = _fullLink

    private val _afId = MutableLiveData<String>()
    val afId: LiveData<String> = _afId

    private val _linkTm = MutableLiveData<String>()
    val linkTm: LiveData<String> = _linkTm

    fun setFullLink(link: String?) {
        _fullLink.postValue(link)
    }

    // Check Internet connection
    fun checkForInternet() {
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connManager.allNetworks
            .map { connManager.getNetworkInfo(it) }
            .firstOrNull { it?.isConnected == true }

        _isConnection.value = networkInfo != null && networkInfo.isAvailable
    }



    //Work with shared preferences
    fun saveFlagZaglushkaInSharedVm() {
        workWithSharedPref.saveFlagZaglushkaInShared("good")
    }

    fun fetchFlagZaglushkaFromSharedVm() = workWithSharedPref.fetchFlagZaglushkaFromShared()

    fun saveFlagLinkInSharedVm() {
        workWithSharedPref.saveFlagLinkInShared("good")
    }

    fun fetchFlagLinkFromSharedVm() = workWithSharedPref.fetchFlagLinkFromShared()

    //Work with fireBase RTDB
    fun fetchDataByLocation() {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val timeZone: String = tm.simCountryIso

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //link listGeo
                try {
                    val data = snapshot.child("app")
                    val localData = data.child("link")
                    val listGeo = data.child("listGeo").value.toString()

                    if (localData != null && listGeo.contains(timeZone, ignoreCase = true)) {
                        _linkTm.value = localData.value.toString()
                    } else {
                        _linkTm.value = ""
                    }
                } catch (e: Exception) {
                    _linkTm.value = ""
                }
                //redirectLink
                try {
                    val data = snapshot.child("app")
                    val redirectLink = data.child("redirectLink").value.toString()

                    if (redirectLink != null) {
                        saveRedirectLinkInShared(redirectLink)
                    } else {
                        saveRedirectLinkInShared("")
                    }
                } catch (e: Exception) {
                    saveRedirectLinkInShared("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _linkTm.value = ""
                saveRedirectLinkInShared("")
            }
        })
    }

    //Check is emulator
    fun isEmulator(buildModel: String = Build.MODEL): Boolean {
        val lowerCaseModel = buildModel.lowercase(Locale.ROOT)
        return lowerCaseModel.startsWith("sdk")
                || lowerCaseModel.startsWith("google_sdk")
                || lowerCaseModel.contains("emulator")
                || lowerCaseModel.contains("android sdk")
                || lowerCaseModel.contains("genymotion")
                || lowerCaseModel.contains("bluestacks")
                || lowerCaseModel.contains("androvm")
                || lowerCaseModel.contains("droid4x")
                || lowerCaseModel.contains("sdk_google")
                || lowerCaseModel.contains("sdk_addon")
                || lowerCaseModel.contains("sdk_arm")
                || lowerCaseModel.contains("vbox86")
                || lowerCaseModel.contains("emulate")
                || lowerCaseModel.contains("x86")
                || lowerCaseModel.contains("virtualbox")
                || lowerCaseModel.contains("youwave")
                || lowerCaseModel.contains("windroy")
                || lowerCaseModel.contains("nox")
                || lowerCaseModel.contains("microsoft")
                || lowerCaseModel.contains("ttvm")
                || lowerCaseModel.contains("msi")
                || lowerCaseModel.contains("noxvm")
                || lowerCaseModel.contains("koplayer")
    }

    fun setAfIdOrganic() {
        _afId.value = UUID.randomUUID().toString()
    }

    //OneSignal
    fun saveStringNotificationOneSignal(str: String) {
        workWithSharedPref.saveStringNotification(str)
    }

    fun getStringNotification() = workWithSharedPref.getStringNotification()

    //FullLink
    fun saveFullLinkInShared(fullLink: String?) {
        if (fullLink != null)
            workWithSharedPref.saveFullLink(fullLink)
    }

    fun getFullLinkFromShared() = workWithSharedPref.fetchFullLink()

    //History save
    fun saveListKeyInShared(key: String, sb: String) {
        workWithSharedPref.saveListKey(key, sb)
    }

    fun getSaveLink(key: String) = workWithSharedPref.getSaveLink(key)

    // redirectLink
    fun getRedirectLinkFromShared() = workWithSharedPref.fetchRedirectLink()

    private fun saveRedirectLinkInShared(redirectLink: String) {
        workWithSharedPref.saveRedirectLink(redirectLink)
    }
}