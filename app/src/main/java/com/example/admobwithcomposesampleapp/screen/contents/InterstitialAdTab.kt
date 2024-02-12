package com.example.admobwithcomposesampleapp.screen.contents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.admobwithcomposesampleapp.viewmodel.AdsViewModel

@Composable
fun InterstitialAdTabContent(
    viewModel: AdsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .clickable {
                    viewModel.loadInterstitialAdIfNeed(context)
                }
                .padding(16.dp),
            text = "インステ広告読み込み開始"
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .clickable {
                    viewModel.showInterstitialAd()
                }
                .padding(16.dp),
            text = "インステ広告表示"
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}