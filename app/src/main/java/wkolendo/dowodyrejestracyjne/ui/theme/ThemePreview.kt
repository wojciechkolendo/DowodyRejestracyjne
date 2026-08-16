package wkolendo.dowodyrejestracyjne.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Previews of the app theme. Nothing here is used by the running app — it exists so the palette,
 * type scale and shapes can be reviewed in Android Studio without launching anything.
 *
 * Previews force [DRTheme] with `dynamicColor = false`, because the wallpaper-derived scheme is
 * only meaningful on a real device.
 */

@Composable
private fun ColorSwatch(name: String, background: Color, foreground: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(text = name, color = foreground, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun ThemeShowcase() {
    val colors = MaterialTheme.colorScheme
    Surface(color = colors.surface) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Dowody Rejestracyjne",
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onSurface,
            )
            Text(
                text = "Open Sans · Material 3",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
            )

            Spacer(Modifier.height(16.dp))

            ColorSwatch("primary", colors.primary, colors.onPrimary)
            ColorSwatch("primaryContainer", colors.primaryContainer, colors.onPrimaryContainer)
            ColorSwatch("secondary", colors.secondary, colors.onSecondary)
            ColorSwatch("secondaryContainer", colors.secondaryContainer, colors.onSecondaryContainer)
            ColorSwatch("surfaceVariant", colors.surfaceVariant, colors.onSurfaceVariant)

            Spacer(Modifier.height(16.dp))

            Card {
                Text(
                    text = "Card · shapes.medium (12dp)",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(onClick = {}) { Text("Skanuj") }

            Spacer(Modifier.height(16.dp))

            Text("titleLarge", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
            Text("bodyLarge", style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
            Text("bodyMedium", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
            Text("labelSmall", style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
        }
    }
}

@Preview(name = "Light", showBackground = true, widthDp = 360)
@Composable
private fun ThemePreviewLight() {
    DRTheme(darkTheme = false, dynamicColor = false) { ThemeShowcase() }
}

@Preview(name = "Dark", showBackground = true, widthDp = 360)
@Composable
private fun ThemePreviewDark() {
    DRTheme(darkTheme = true, dynamicColor = false) { ThemeShowcase() }
}
