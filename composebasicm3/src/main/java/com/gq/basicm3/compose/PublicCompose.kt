package com.gq.basicm3.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gq.basicm3.R
import com.gq.basicm3.theme.DividerColor


/**
 * 弹窗下面的双按钮
 */
@Composable
fun DialogBottomDoubleButton2(
    cancelText: String = stringResource(id = R.string.cb_do_not_use),
    confirmText: String = stringResource(id = R.string.cb_agree),
    cancelTextColor: Color = MaterialTheme.colorScheme.onSurface,
    confirmTextColor: Color = MaterialTheme.colorScheme.primary,
    cancelTextSize: TextUnit = 14.sp,
    confirmTextSize: TextUnit = 14.sp,
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    Column {
        Divider(color = MaterialTheme.colorScheme.DividerColor, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onClick = {
                    onCancelClick()
                }
            ) {
                Text(text = cancelText, color = cancelTextColor, fontSize = cancelTextSize)
            }
            Spacer(modifier = Modifier
                .fillMaxHeight()
                .width(0.5.dp)
                .background(color = MaterialTheme.colorScheme.DividerColor))
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onClick = {
                    onConfirmClick()
                }
            ) {
                Text(text = confirmText, color = confirmTextColor, fontSize = confirmTextSize)
            }
        }
    }
}