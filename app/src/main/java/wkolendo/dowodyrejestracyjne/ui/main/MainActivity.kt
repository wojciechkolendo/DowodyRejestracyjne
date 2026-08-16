package wkolendo.dowodyrejestracyjne.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import wkolendo.dowodyrejestracyjne.repository.SettingsRepository
import wkolendo.dowodyrejestracyjne.ui.DRApp
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme
import wkolendo.dowodyrejestracyjne.ui.theme.isDark

/**
 * The app's only activity: an entry point for the system, not a screen.
 *
 * [enableEdgeToEdge] makes the app draw behind the system bars, which Android enforces anyway for
 * apps targeting API 35+. `Scaffold` in each screen turns those insets into content padding, which
 * is why the toolbar no longer sits underneath the status bar.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by SettingsRepository.themeMode.collectAsStateWithLifecycle()
            val darkTheme = themeMode.isDark()

            // The system bar icons follow the system theme by default. With a forced light or dark
            // mode that leaves them unreadable, so they are told which scheme is actually on screen.
            LaunchedEffect(darkTheme) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            DRTheme(darkTheme = darkTheme) {
                // The window theme is DayNight, so it follows the system rather than the mode picked
                // in settings. Forcing dark on a light phone would otherwise leave a white window
                // behind the app — visible for the first frame and through destination crossfades.
                val background = MaterialTheme.colorScheme.background
                LaunchedEffect(background) {
                    window.setBackgroundDrawable(background.toArgb().toDrawable())
                }

                DRApp()
            }
        }
    }
}
