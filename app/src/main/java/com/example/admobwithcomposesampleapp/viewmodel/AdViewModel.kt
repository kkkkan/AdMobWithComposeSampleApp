package com.example.admobwithcomposesampleapp.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.admobwithcomposesampleapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 広告関係のViewModel
 */
@HiltViewModel
class AdsViewModel @Inject constructor(
) : ViewModel() {
    data class UiState(
        // インステ広告を表示する
        val showInterstitialAd: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()


    // インタースティシャル広告
    // プロセス再生成には対応していません。
    private var interstitialAd: InterstitialAd? = null

    private var fullScreenContentCallback: FullScreenContentCallback? = null

    private var isInterstitialAdNowLoading = false

    // インステ広告表示のフラグを立てる
    fun showInterstitialAd() {
        _uiState.value = _uiState.value.copy(showInterstitialAd = true)
    }

    fun consumedInterstitialAd() {
        _uiState.value = _uiState.value.copy(showInterstitialAd = false)
    }

    /**
     * インタースティシャル広告を読み込み開始
     */
    fun loadInterstitialAdIfNeed(ctx: Context) {
        if (isInterstitialAdNowLoading || interstitialAd != null) {
            // 既に読み込み済みor読み込み中だったら、何もしない
            return
        }
        // 読み込み開始のフラグを立てる
        isInterstitialAdNowLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            ctx,
            ctx.getString(R.string.admob_interstitial_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // フラグを下す
                    isInterstitialAdNowLoading = false
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    // フラグを下す
                    isInterstitialAdNowLoading = false

                    interstitialAd = ad
                    interstitialAd!!.fullScreenContentCallback = fullScreenContentCallback
                }
            })
    }

    /**
     * 可能なタイミングと状況なら、インタースティシャル広告を見せる。
     */
    fun openInterstitialAdIfCan(activity: Activity) {
        if (interstitialAd != null) {
            // 共有直後かつインタースティシャル広告の読み込みが終わっていれば表示。
            // フラグを下したりは、interstitialAdの表示コールバックの方で行っている。
            interstitialAd!!.show(activity)
        }
    }

}