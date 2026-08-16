package wkolendo.dowodyrejestracyjne.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.getSystemService
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.details.DetailsScreen
import wkolendo.dowodyrejestracyjne.ui.details.toShareableText
import wkolendo.dowodyrejestracyjne.ui.navigation.DetailsKey
import wkolendo.dowodyrejestracyjne.ui.navigation.SettingsKey
import wkolendo.dowodyrejestracyjne.ui.navigation.StartKey
import wkolendo.dowodyrejestracyjne.ui.settings.SettingsScreen
import wkolendo.dowodyrejestracyjne.ui.start.StartScreen
import wkolendo.dowodyrejestracyjne.utils.logError

/**
 * Root of the app: owns the back stack and maps each destination to its screen.
 *
 * In Navigation 3 the back stack is a plain observable list, so "go back" is `removeLastOrNull()`
 * and there is no graph to declare up front.
 */
@Composable
fun DRApp(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(StartKey)
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // MaterialTheme only supplies values, it paints nothing. Without an opaque layer here the
    // crossfade between destinations has both screens partly transparent at once and the window
    // shows through, which reads as a flash of the wrong colour.
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider<NavKey> {
                entry<StartKey> {
                    StartScreen(
                        onOpenDetails = { certificate -> backStack.add(DetailsKey(certificate)) },
                        onOpenSettings = { backStack.add(SettingsKey) },
                    )
                }

                entry<DetailsKey> { key ->
                    val copiedMessage = stringResource(R.string.details_copy_success_message)
                    fun copy(text: String) {
                        context.copyToClipboard(text)
                        scope.launch { snackbarHostState.showSnackbar(copiedMessage) }
                    }

                    DetailsScreen(
                        certificate = key.certificate,
                        onBack = { backStack.removeLastOrNull() },
                        onCopy = { copy(key.certificate.toShareableText(context.resources)) },
                        onShare = { context.shareCertificate(key.certificate) },
                        onCopyValue = ::copy,
                        snackbarHostState = snackbarHostState,
                    )
                }

                entry<SettingsKey> {
                    SettingsScreen(onBack = { backStack.removeLastOrNull() })
                }
            },
        )
    }
}

private fun Context.copyToClipboard(text: String) {
    runCatching {
        getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText("label", text))
    }.onFailure { logError(it) }
}

/** A device with no app able to receive text throws ActivityNotFoundException from the chooser. */
private fun Context.shareCertificate(certificate: Certificate) {
    runCatching {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, certificate.toShareableText(resources))
                },
                getString(R.string.details_share),
            )
        )
    }.onFailure { logError(it) }
}
