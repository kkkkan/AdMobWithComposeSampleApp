package com.example.admobwithcomposesampleapp.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.admobwithcomposesampleapp.Top
import com.example.admobwithcomposesampleapp.screen.contents.BannerTabContent
import com.example.admobwithcomposesampleapp.screen.contents.InterstitialAdTabContent
import com.example.admobwithcomposesampleapp.screen.contents.NativeAdTabContent
import com.example.admobwithcomposesampleapp.screen.contents.RewardAdTabContent
import com.example.admobwithcomposesampleapp.utils.LocalActivity
import com.example.admobwithcomposesampleapp.viewmodel.InterstitialAdViewModel
import com.example.admobwithcomposesampleapp.viewmodel.RewardAdViewModel
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun TopScreen(
    // 使用するバナー広告
    // リストのスクロール量が変更して表示非表示が切り替わった場合に毎回loadAdを呼んだりしないように
    // Activityが作成された時に作って、以降はそれを使いまわす
    banner: AdView,
    nativeAdView: NativeAdView,
    interstitialAdViewModel: InterstitialAdViewModel = hiltViewModel(),
    rewardAdViewModel: RewardAdViewModel = hiltViewModel()
) {
    val interstitialAdState = interstitialAdViewModel.uiState.collectAsState()
    val rewardAdState = rewardAdViewModel.uiState.collectAsState()

    val activity = LocalActivity.current
    LaunchedEffect(key1 = interstitialAdState.value.showInterstitialAd) {
        if (interstitialAdState.value.showInterstitialAd) {
            // インステ広告表示フラグがたったら、表示する
            interstitialAdViewModel.openInterstitialAdIfCan(activity)
            interstitialAdViewModel.consumedInterstitialAd()
        }
    }

    LaunchedEffect(key1 = rewardAdState.value.showRewardAd) {
        if (rewardAdState.value.showRewardAd) {
            // リワード広告表示フラグがたったら、表示する
            rewardAdViewModel.openRewardAdIfCan(activity){
                Toast.makeText(activity, "リワード獲得", Toast.LENGTH_SHORT).show()
            }
            rewardAdViewModel.consumedRewardAd()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val selectedTab = remember {
            // 最初に表示するのはバナー広告のタブ
            mutableStateOf(Top.Tabs.Banner.tabIndex)
        }


        // タブ
        TopScreenTabs(
            selectedTabIndex = selectedTab.value,
            onSelectedTab = {
                selectedTab.value = it
            }
        )



        when (selectedTab.value) {
            Top.Tabs.Banner.tabIndex -> {
                BannerTabContent(banner = banner)
            }

            Top.Tabs.Native.tabIndex -> {
                NativeAdTabContent(nativeAd = nativeAdView)
            }

            Top.Tabs.Interstitial.tabIndex -> {
                InterstitialAdTabContent()
            }

            Top.Tabs.Reward.tabIndex -> {
                RewardAdTabContent()
            }
        }
    }
}


@Composable
private fun TopScreenTabs(
    selectedTabIndex: Int,
    onSelectedTab: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        // タブ
        val tabs = ArrayList<Top.Tabs>().apply {
            addAll(Top.Tabs.values())
            sortBy { it.tabIndex }
        }

        for (tab in tabs) {
            Tab(
                text = {
                    Text(text = tab.tabName)
                },
                selected = selectedTabIndex == tab.tabIndex,
                onClick = {
                    onSelectedTab(tab.tabIndex)
                }
            )
        }
    }
}