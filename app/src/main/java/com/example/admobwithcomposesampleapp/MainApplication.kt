package com.example.admobwithcomposesampleapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // AdMobSDKのイニシャライズ
        MobileAds.initialize(this)
    }
}