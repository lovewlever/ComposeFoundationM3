package com.gq.basic.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.gq.basic.ui.compose.HomePageCompose
import com.gq.basicm3.compose.LogListCompose

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimNavHost(navController: NavHostController) {
    AnimatedNavHost(navController = navController, startDestination = NavRouter.HomePage.route) {
        composable(route = NavRouter.HomePage.route) {
            HomePageCompose(navController)
        }
        composable(route = NavRouter.LogPage.route) {
            LogListCompose()
        }
    }
}