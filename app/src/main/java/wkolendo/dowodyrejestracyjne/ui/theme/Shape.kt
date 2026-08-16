package wkolendo.dowodyrejestracyjne.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Corner radii carried over from `res/values/dimens.xml`.
 *
 * `medium` covers what the XML theme spread across `cardCornerRadius`, `boxCornerRadius` and
 * `dialogCornerRadius` (all 12dp); `large` matches `buttonCornerRadius` (16dp).
 */
val DRShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
