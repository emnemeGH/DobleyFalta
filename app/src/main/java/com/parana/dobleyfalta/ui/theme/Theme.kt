package com.parana.dobleyfalta.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val CompactTypography = Typography(
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
)

@Composable
fun DobleYFaltaTheme(content: @Composable () -> Unit) {
    val darkColorScheme = darkColorScheme(
        background = Color.Black,
        onPrimary = Color.Black,
        error = Color(0xFFCC1F3F)
    )

    MaterialTheme(
        colorScheme = darkColorScheme,
        typography = CompactTypography,
        content = content
    )
}