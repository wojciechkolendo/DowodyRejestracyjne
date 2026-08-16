package wkolendo.dowodyrejestracyjne.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import wkolendo.dowodyrejestracyjne.ui.DRApp
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme

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
            DRTheme {
                DRApp()
            }
        }
    }
}
