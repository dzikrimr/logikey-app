package com.example.logiclyst.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.logiclyst.R

val Roboto = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_italic, style = FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_mediumitalic, FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bolditalic, FontWeight.Bold, style = FontStyle.Italic),
)

val RobotoCondensed = FontFamily(
    Font(R.font.roboto_condensed_regular),
    Font(R.font.roboto_condensed_italic, style = FontStyle.Italic),
    Font(R.font.roboto_condensed_bold, FontWeight.Bold),
    Font(R.font.roboto_condensed_bolditalic, FontWeight.Bold, style = FontStyle.Italic),
)

private val DefaultTypography = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = RobotoCondensed),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = RobotoCondensed),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = RobotoCondensed),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = RobotoCondensed),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = RobotoCondensed),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = RobotoCondensed),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = Roboto),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = Roboto),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = Roboto),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = Roboto),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = Roboto),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = Roboto),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = Roboto),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = Roboto),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = Roboto)
)
