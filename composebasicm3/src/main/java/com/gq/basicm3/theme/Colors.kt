package com.gq.basicm3.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val ColorScheme.DividerColor: Color
    @Composable get() = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer.copy(red = 0.15F, green = 0.15F, blue = 0.15F)
    else MaterialTheme.colorScheme.primaryContainer.copy(red = 0.9F, green = 0.9F, blue = 0.9F)