package com.onecab.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.onecab.core.R

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.7.sp,
        color = Color(0xFF09224B)
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.7.sp,
        color = Color(0xFF09224B)
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.myriadpro_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = Color(0xFF09224B)
    ),
)