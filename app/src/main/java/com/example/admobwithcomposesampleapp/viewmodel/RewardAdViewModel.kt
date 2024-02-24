package com.example.admobwithcomposesampleapp.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.admobwithcomposesampleapp.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * リワード広告関係のViewModel
 */
@HiltViewModel
class RewardAdViewModel @Inject constructor(
) : ViewModel() {
    data class UiState(
        // 広告が表示できる状態かどうか
        val canShowAd: Boolean = false,
        // リワード広告を表示する
        val showRewardAd: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()


    // リワード広告
    // プロセス再生成には対応していません。
    private var rewardAd: RewardedAd? = null
        set(value) {
            field = value
            _uiState.value = _uiState.value.copy(canShowAd = (value != null))
        }

    private var fullScreenContentCallback: FullScreenContentCallback? = null

    private var isRewardAdNowLoading = false

    // リワード広告表示のフラグを立てる
    fun showRewardAd() {
        _uiState.value = _uiState.value.copy(showRewardAd = true)
    }

    fun consumedRewardAd() {
        _uiState.value = _uiState.value.copy(showRewardAd = false)
    }

    /**
     * リワード広告を読み込み開始
     */
    fun loadRewardAdIfNeed(ctx: Context) {
        if (isRewardAdNowLoading || rewardAd != null) {
            // 既に読み込み済みor読み込み中だったら、何もしない
            return
        }

        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                // 表示に失敗しても、おそらく広告の再利用はできないので捨てる。
                rewardAd = null
            }

            override fun onAdShowedFullScreenContent() {
                // 表示を開始
                // 広告の再利用はできないので捨てる。
                rewardAd = null
            }
        }

        // 読み込み開始のフラグを立てる
        isRewardAdNowLoading = true
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            ctx,
            ctx.getString(R.string.admob_reward_unit_id),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // フラグを下す
                    isRewardAdNowLoading = false
                    rewardAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    // フラグを下す
                    isRewardAdNowLoading = false

                    rewardAd = ad
                    rewardAd!!.fullScreenContentCallback = fullScreenContentCallback
                }
            })
    }

    /**
     * 可能なタイミングと状況なら、リワード広告を見せる。
     */
    fun openRewardAdIfCan(
        activity: Activity,
        onEarnedReward: (rewardItem: RewardItem) -> Unit
    ) {
        if (rewardAd != null) {
            // 共有直後かつインタースティシャル広告の読み込みが終わっていれば表示。
            // フラグを下したりは、RewardAdの表示コールバックの方で行っている。
            rewardAd!!.show(activity) { rewardItem ->
                // TODO
                // リワード広告を見た後の処理
                // ここでリワードを付与する処理を行う
                onEarnedReward(rewardItem)
            }
        }
    }

}