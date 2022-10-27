package com.gq.basicm3.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.gq.basicm3.R
import com.gq.basicm3.common.DataStoreCommon
import kotlinx.coroutines.launch

const val PermissionConfirmAgree = 0
const val PermissionConfirmReject = -1
const val PermissionConfirmNotSet = -2

/**
 * App第一次启动 权限申请提醒
 */
@Composable
fun PermissionConfirmationCompose(
    permissionContent: @Composable () -> Unit,
    onDoneClick: () -> Unit,
    onRejectClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var isShowDialog by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true, block = {
        val i = DataStoreCommon.getBasicType(
            DataStoreCommon.DSK_PERMISSION_CONFIRM,
            PermissionConfirmNotSet
        )
        if (i == PermissionConfirmNotSet) {
            isShowDialog = true
        }
    })
    if (isShowDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(14.dp))
                Text(text = "权限申请", fontSize = 16.sp)
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    permissionContent()
                }
                Spacer(Modifier.height(14.dp))
                DialogBottomDoubleButton2(
                    cancelText = stringResource(id = R.string.cb_reject),
                    confirmText = stringResource(id = R.string.cb_agree),
                    onCancelClick = {
                        coroutineScope.launch {
                            DataStoreCommon.putBasicType(
                                DataStoreCommon.DSK_PERMISSION_CONFIRM,
                                PermissionConfirmReject
                            )
                            onRejectClick()
                            isShowDialog = false
                        }
                    },
                    onConfirmClick = {
                        coroutineScope.launch {
                            DataStoreCommon.putBasicType(
                                DataStoreCommon.DSK_PERMISSION_CONFIRM,
                                PermissionConfirmAgree
                            )
                            onDoneClick()
                            isShowDialog = false
                        }
                    }
                )
            }
        }
    }
}