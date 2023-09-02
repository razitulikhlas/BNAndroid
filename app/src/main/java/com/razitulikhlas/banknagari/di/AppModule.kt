package com.razitulikhlas.banknagari.di

import com.google.android.gms.location.LocationServices
import com.razitulikhlas.banknagari.adapter.*
import com.razitulikhlas.banknagari.ui.calculator.TableCalculatorViewModel
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardViewModel
import com.razitulikhlas.banknagari.ui.login.LoginViewModel
import com.razitulikhlas.banknagari.ui.permohonan.OfficerViewModel
import com.razitulikhlas.banknagari.viewmodel.CustomerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { CustomerViewModel(get()) }
    viewModel { TableCalculatorViewModel(application = androidApplication()) }
    viewModel { DashBoardViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { OfficerViewModel(get()) }

}

val adapterModule = module {
    single { CustomerAdapter() }
    single { TableCalculatorAdapter() }
    single { MappingCustomerAdapter() }
    single { DashboardSkimAdapter() }
    single { DataSkimAdapter() }

}

val locationModule = module {
//    single { FusedLocationProviderClient() }
    single { LocationServices.getSettingsClient(androidContext()) }
//    single {
//        LocationSettingsRequest.Builder().apply {
//            addLocationRequest(get())
//            setAlwaysShow(true)
//        }.build()
//    }
}