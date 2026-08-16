package wkolendo.dowodyrejestracyjne.ui.settings

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.logError

/** Opens a mail client addressed to the support address. */
fun Context.openEmailChooser() {
    runCatching {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_contact_summary)))
                },
                getString(R.string.settings_send_email),
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }.onFailure { logError(it) }
}

/** Opens the app in the Play Store, falling back to the web listing when the store is missing. */
fun Context.openGooglePlay() {
    runCatching {
        startActivity(
            Intent(Intent.ACTION_VIEW, "market://details?id=${BuildConfig.APPLICATION_ID}".toUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }.recoverCatching {
        logError(it)
        startActivity(
            Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".toUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }.onFailure { logError(it) }
}
