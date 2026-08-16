package wkolendo.dowodyrejestracyjne.utils

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Screen names reported to Analytics.
 *
 * Automatic screen reporting is switched off in the manifest: the app is a single activity, so it
 * would report `MainActivity` for everything and tell us nothing about which screen is used.
 */
object Screen {
    const val START = "start"
    const val DETAILS = "details"
    const val SETTINGS = "settings"
    const val SCANNER = "scanner"
    const val DIALOG_CLEAR_HISTORY = "dialog_clear_history"
    const val DIALOG_CAMERA_PERMISSION = "dialog_camera_permission"
    const val DIALOG_SCAN_ERROR = "dialog_scan_error"
}

/**
 * Reports a screen view once per entry into composition.
 *
 * Dialogs get their own names so their usage is visible separately from the screen behind them.
 * Only the screen name is ever sent — never anything read off a certificate.
 */
@Composable
fun TrackScreen(screenName: String) {
    val context = LocalContext.current
    LaunchedEffect(screenName) {
        context.logScreenView(screenName)
    }
}

private fun Context.logScreenView(screenName: String) {
    runCatching {
        FirebaseAnalytics.getInstance(this).logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
            },
        )
    }.onFailure { logError(it) }
}
