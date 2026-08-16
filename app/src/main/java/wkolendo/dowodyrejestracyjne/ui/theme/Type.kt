package wkolendo.dowodyrejestracyjne.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import wkolendo.dowodyrejestracyjne.R

/** Open Sans, the same four weights the XML theme referenced through `@font/open_sans`. */
val OpenSans = FontFamily(
    Font(R.font.open_sans_light, FontWeight.Light),
    Font(R.font.open_sans, FontWeight.Normal),
    Font(R.font.open_sans_semibold, FontWeight.SemiBold),
    Font(R.font.open_sans_bold, FontWeight.Bold),
)

/**
 * Material 3 type scale with Open Sans applied to every style.
 *
 * Material 3 dropped the `defaultFontFamily` parameter that Material 2 had, so each style has to be
 * copied explicitly. Sizes and line heights stay at their Material 3 defaults.
 */
private val default = Typography()

val DRTypography = Typography(
    displayLarge = default.displayLarge.copy(fontFamily = OpenSans),
    displayMedium = default.displayMedium.copy(fontFamily = OpenSans),
    displaySmall = default.displaySmall.copy(fontFamily = OpenSans),
    headlineLarge = default.headlineLarge.copy(fontFamily = OpenSans),
    headlineMedium = default.headlineMedium.copy(fontFamily = OpenSans),
    headlineSmall = default.headlineSmall.copy(fontFamily = OpenSans),
    titleLarge = default.titleLarge.copy(fontFamily = OpenSans),
    titleMedium = default.titleMedium.copy(fontFamily = OpenSans),
    titleSmall = default.titleSmall.copy(fontFamily = OpenSans),
    bodyLarge = default.bodyLarge.copy(fontFamily = OpenSans),
    bodyMedium = default.bodyMedium.copy(fontFamily = OpenSans),
    bodySmall = default.bodySmall.copy(fontFamily = OpenSans),
    labelLarge = default.labelLarge.copy(fontFamily = OpenSans),
    labelMedium = default.labelMedium.copy(fontFamily = OpenSans),
    labelSmall = default.labelSmall.copy(fontFamily = OpenSans),
)
