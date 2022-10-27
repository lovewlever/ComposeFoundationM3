package com.gq.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.gq.basic.ui.navigation.AnimNavHost
import com.gq.basic.ui.theme.ComposeFoundationM3Theme
import com.gq.basicm3.basis.BasicActivity
import com.gq.basicm3.compose.PrivacyPolicyConfirmationDialogCompose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeFoundationM3Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Greeting(name: String) {
    val navController = rememberAnimatedNavController()
    Scaffold(
        /*topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "material3")
                })
        },*/
        bottomBar = {
            NavigationBar() {
                NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null)
                }, label = {
                    Text(text = "首页")
                })
            }
        }
    ) {
        Box(modifier = Modifier.padding(it))
        AnimNavHost(navController = navController)
    }

}