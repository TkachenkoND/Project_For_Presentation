package com.online.bk.olimpbet.di

import com.online.bk.olimpbet.presentation.view_model.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {  SharedViewModel(get(), get(), get()) }
}