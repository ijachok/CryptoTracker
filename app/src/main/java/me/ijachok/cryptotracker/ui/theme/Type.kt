package me.ijachok.cryptotracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.R

val bodyFontFamily = FontFamily(
    Font(
        resId = R.font.space_grotesk_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.space_grotesk_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.space_grotesk_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.space_grotesk_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.space_grotesk_light,
        weight = FontWeight.Light
    ),
)

val displayFontFamily = FontFamily(
    Font(
        resId = R.font.space_mono_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.space_mono_bolditalic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.space_mono_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.space_mono_regular,
        weight = FontWeight.Normal
    )
)

val CryptoTrackerTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
    ),
    headlineMedium = TextStyle(
        fontFamily = displayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)

