package com.razitulikhlas.banknagari

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.razitulikhlas.banknagari.di.adapterModule
import com.razitulikhlas.banknagari.di.locationModule
import com.razitulikhlas.banknagari.di.viewModule
import com.razitulikhlas.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    databaseModule,
                    repositoryModule,
                    retrofitModule,
                    localDataSourceSourceModule,
                    useCaseModule,
                    viewModule,
                    locationModule,
                    adapterModule,
                    remoteDataSourceModule
                )
            )
        }
    }
}