package com.gq.basic.ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.gq.basic.ui.navigation.NavRouter
import com.gq.basicm3.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageCompose(navController: NavController) {
    val checkUpdateState = rememberCheckUpdateState()
    val loadingDialogState = rememberLoadingDialogState(isShow = false)
    val progressButtonState = rememberProgressButtonState()
    val toastCustomState = rememberToastCustomState()
    Scaffold {
        FlowRow(modifier = Modifier
            .padding(it)
            .padding(horizontal = 8.dp)
            .padding(top = 80.dp)) {
            Button(onClick = {

            },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Text(text = "协议弹窗")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                checkUpdateState.showDialog("v1.1.0",
                    "1.更新说明，\n2.更新说明更新说明更\n3.新说明更新说明更新说明",
                    "https")
            },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(text = "CheckUpdate")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Switch(checked = true, onCheckedChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                loadingDialogState.showLoading("加载中...")
            },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Text(text = "LoadingDialog")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                navController.navigate(NavRouter.LogPage.route)
            },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
                Text(text = "Log")
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProgressButton(width = 80.dp, height = 40.dp, state = progressButtonState, onClick = {
                progressButtonState.isLoading = true
            }) {
                Text(text = "ProgressBTn")
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProgressButton(width = 80.dp, height = 40.dp, state = progressButtonState, onClick = {
                toastCustomState.showToast("CustomToast")
            }) {
                Text(text = "CusToast")
            }
        }

        CheckUpdateAppCompose(checkUpdateState = checkUpdateState)

        PrivacyPolicyConfirmationDialogCompose(onUserAgreementClick = { /*TODO*/ },
            onPrivacyPolicyClick = {},
            doneClick = {

            })

        LoadingDialogCompose(loadingDialogState = loadingDialogState)

        PermissionConfirmationCompose(permissionContent = {
            Text(text = "1. 我们将申请权限")
        }, onDoneClick = {}, onRejectClick = {})

        ToastCustomCompose(toastCustomState = toastCustomState)
    }
}