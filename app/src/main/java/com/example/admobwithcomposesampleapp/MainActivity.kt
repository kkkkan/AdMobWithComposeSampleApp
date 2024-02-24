package com.example.admobwithcomposesampleapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.admobwithcomposesampleapp.databinding.NativeAdLayoutBinding
import com.example.admobwithcomposesampleapp.screen.TopScreen
import com.example.admobwithcomposesampleapp.ui.theme.AdMobWithComposeSampleAppTheme
import com.example.admobwithcomposesampleapp.utils.LocalActivity
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var nativeAdLayoutBinding: NativeAdLayoutBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val banner = AdView(this)
        banner.setAdSize(AdSize.MEDIUM_RECTANGLE)
        banner.adUnitId = getString(R.string.admob_banner_unit_id)
        banner.loadAd(AdRequest.Builder().build())

        // ネイティブ広告の初期化
        initNativeAd()

        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                // LocalActivityを取得できるようにする
                AdMobWithComposeSampleAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationHost(
                            banner = banner,
                            nativeAdView = nativeAdLayoutBinding!!.root as NativeAdView
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 広告の解放
        (nativeAdLayoutBinding?.root as? NativeAdView)?.destroy()
    }


    /**
     * ネイティブ広告の初期化
     */
    private fun initNativeAd() {
        nativeAdLayoutBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.native_ad_layout,
            null,
            false
        )
        AdLoader.Builder(this, getString(R.string.admob_native_unit_id))
            .forNativeAd { ad ->
                val nativeAdView = nativeAdLayoutBinding!!.root as NativeAdView

                nativeAdView.mediaView = nativeAdLayoutBinding!!.adMedia
                nativeAdLayoutBinding!!.adMedia.setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                nativeAdView.headlineView = nativeAdLayoutBinding!!.adHeadline
                nativeAdLayoutBinding!!.adHeadline.text = ad.headline
                nativeAdView.bodyView = nativeAdLayoutBinding!!.adBody
                nativeAdLayoutBinding!!.adBody.text = ad.body
                nativeAdView.adChoicesView = nativeAdLayoutBinding!!.adChoices
                nativeAdView.advertiserView = nativeAdLayoutBinding!!.adAdvertiser
                nativeAdLayoutBinding!!.adAdvertiser.text = ad.advertiser
                nativeAdView.setNativeAd(ad)
            }.build().loadAd(AdRequest.Builder().build())
    }
}


@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    // 使用するバナー広告
    // リストのスクロール量が変更して表示非表示が切り替わった場合に毎回loadAdを呼んだりしないように
    // Activityが作成された時に作って、以降はそれを使いまわす
    banner: AdView,
    nativeAdView: NativeAdView,
    startDestination: String = Top.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Top.route) {
            TopScreen(
                banner,
                nativeAdView
            )
        }
    }
}