package com.example.admobwithcomposesampleapp.screen.contents

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.admobwithcomposesampleapp.R
import com.example.admobwithcomposesampleapp.screen.contents.util.makeDummyDataList
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAdTabContent(
    // 使用するネイティブ広告
    // リストのスクロール量が変更して表示非表示が切り替わった場合に毎回loadAdを呼んだりしないように
    // Activityが作成された時に作って、以降はそれを使いまわす
    nativeAd: NativeAdView
) {
    val dummyItems = remember {
        makeDummyDataList()
    }

    // コンテンツリストのうち、バナー広告を追加したいindex
    // 5個目にバナーを入れる
    val bannerIndex = remember {
        5
    }

    LazyColumn {
        items(dummyItems.size + 1) { index ->
            if (index == bannerIndex) {
                // 広告自体は、スクロールが進んでから戻ってきて画面に再度表示されるときに再度リクエストを送るようにしすると、loadAdをそのたびに呼ぶことになってしまいよくないので
                // 一度作成した広告を再利用するようにする
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        // 縦方向にはpaddingをつける
                        .padding(vertical = 8.dp),
                    factory = { context ->
                        if (nativeAd.parent != null) {
                            // すでに親がいる場合は消す
                            // 直接AndroidViewのfactoryの戻りにせずFrameLayoutにくるむことで
                            // 親からリムーブ出来る。
                            // (スクロールが進んで一度画面から外れてから再度追加される場合に、まずは親からリムーブしないと、
                            // Fatal Exception: java.lang.IllegalStateException
                            // The specified child already has a parent. You must call removeView() on the child's parent first.
                            // という例外が発生する)
                            (nativeAd.parent as FrameLayout).removeAllViews()
                        }
                        FrameLayout(context).apply {
                            // 縦の大きさを整える
                            minimumHeight =
                                context.resources.getDimensionPixelSize(R.dimen.native_ad_height)
                            addView(nativeAd)
                        }
                    },
                )
            } else {
                val listItemIndex = if (index >= bannerIndex) {
                    // バナー広告の分を引く
                    index - 1
                } else {
                    index
                }
                val item = dummyItems[listItemIndex]
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        // 縦方向にはpaddingをつける
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    text = item
                )
            }

            Divider()
        }
    }
}
