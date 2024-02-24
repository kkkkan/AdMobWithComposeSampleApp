package com.example.admobwithcomposesampleapp.screen.contents.util

/**
 * ダミーデータを作成する
 */
fun makeDummyDataList(): List<String> {
    return arrayListOf<String>().apply {
        for (i in 0..100) {
            this.add("アイテム　$i")
        }
    }
}