package com.gq.basic.ui.navigation

sealed class NavRouter(
    val route: String,
    val title: String,
    val selBtnResId: Int = 0,
    val unSelBtnResId: Int = 0,
) {
    object HomePage: NavRouter("HomePage", "HomePage", 0, 0)
    object MinePage: NavRouter("MinePage", "MinePage", 0, 0)
    object LogPage: NavRouter("LogPage", "LogPage", 0, 0)
}
