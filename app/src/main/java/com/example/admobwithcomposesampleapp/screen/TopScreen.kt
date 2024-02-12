package com.example.admobwithcomposesampleapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.admobwithcomposesampleapp.Top

@Composable
fun TopScreen() {
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
                // TODO
            }

            Top.Tabs.Native.tabIndex -> {
                // TODO
            }

            Top.Tabs.Interstitial.tabIndex -> {
                // TODO
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