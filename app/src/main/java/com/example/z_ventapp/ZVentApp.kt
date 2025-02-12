package com.example.z_ventapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZVentApp : Application() {

    // /* Inicializar la Publicidad
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)
    }
    //
}