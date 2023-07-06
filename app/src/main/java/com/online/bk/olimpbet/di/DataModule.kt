package com.online.bk.olimpbet.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.online.bk.olimpbet.data.shared_preferences.WorkWithSharedPref
import org.koin.dsl.module

val dataModule = module {
    single { WorkWithSharedPref(get()) }
    single { Firebase.database.reference }
}