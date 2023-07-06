package com.online.bk.olimpbet.data.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import com.online.bk.olimpbet.data.helper.APP_PREFERENCES
import com.online.bk.olimpbet.data.helper.FULL_LINK
import com.online.bk.olimpbet.data.helper.LINK
import com.online.bk.olimpbet.data.helper.REDIRECT_LINK
import com.online.bk.olimpbet.data.helper.STR
import com.online.bk.olimpbet.data.helper.ZAGLUSHKA

class WorkWithSharedPref(context: Context) {
    private var shared: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = shared.edit()

    fun saveFlagZaglushkaInShared(flag: String?) {
        editor.putString(ZAGLUSHKA, flag)
        editor.apply()
    }

    fun fetchFlagZaglushkaFromShared() = shared.getString(ZAGLUSHKA, "")

    fun saveFlagLinkInShared(countryCode: String?) {
        editor.putString(LINK, countryCode)
        editor.apply()
    }

    fun fetchFlagLinkFromShared() = shared.getString(LINK, "")

    fun fetchFullLink() = shared.getString(FULL_LINK, "")

    fun saveFullLink(fullLink: String) {
        editor.putString(FULL_LINK, fullLink)
        editor.apply()
    }

    //User History
    fun saveListKey(key: String, sb: String) {
        editor.putString(key, sb)
        editor.commit()
    }

    fun getSaveLink(key: String) = shared.getString(key, "")

    //OneSignal
    fun saveStringNotification(str: String) {
        editor.putString(STR, str)
        editor.apply()
    }

    fun getStringNotification() = shared.getString(STR, "")

    fun fetchRedirectLink() = shared.getString(REDIRECT_LINK, "")

    fun saveRedirectLink(redirectLink: String) {
        editor.putString(REDIRECT_LINK, redirectLink)
        editor.apply()
    }
}