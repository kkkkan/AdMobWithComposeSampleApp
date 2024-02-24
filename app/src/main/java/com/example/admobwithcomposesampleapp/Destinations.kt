package com.example.admobwithcomposesampleapp

interface Destinations {
    val route: String
}

object Top : Destinations {
    override val route = "top"

    enum class Tabs(
        val tabName: String,
        val tabIndex: Int
    ) {
        Banner("バナー", 0),
        Native("ネイティブ", 1),
        Interstitial("インタースティシャル", 2),
        Reward("リワード", 3)
    }
}