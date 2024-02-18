package com.example.admobwithcomposesampleapp.screen.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.admobwithcomposesampleapp.viewmodel.AdsViewModel

@Composable
fun InterstitialAdTabContent(
    viewModel: AdsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState()
    Column {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = Color.Red
                )
                .clickable {
                    viewModel.loadInterstitialAdIfNeed(context)
                }
                .padding(16.dp),
            style = androidx.compose.ui.text.TextStyle(
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            text = "インステ広告読み込み開始"
        )
        Spacer(modifier = Modifier.weight(1f))

        // 広告を表示ボタンの背景色は広告を読み込み済みかどうかで変える
        val showButtonBackgroundColor = if (state.value.canShowAd) {
            Color.Green
        } else {
            Color.Gray
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = showButtonBackgroundColor
                )
                .clickable {
                    if (state.value.canShowAd) {
                        // 広告を読み込み済みだったら表示する
                        viewModel.showInterstitialAd()
                    }
                }
                .padding(16.dp),
            style = androidx.compose.ui.text.TextStyle(
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            text = "インステ広告表示"
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}