package com.example.playstore.model

data class AppRow(
    val title: String,
    val apps: List<AppItem>,
    val layoutType: LayoutType
) {
    enum class LayoutType {
        VERTICAL_LIST,
        HORIZONTAL_STRIP
    }
}
