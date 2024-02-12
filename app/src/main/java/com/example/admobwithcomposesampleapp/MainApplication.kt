package com.example.admobwithcomposesampleapp

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // AdMobSDKのイニシャライズ
        MobileAds.initialize(this)
    }
}