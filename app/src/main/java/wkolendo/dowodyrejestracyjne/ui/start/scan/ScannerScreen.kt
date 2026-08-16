package wkolendo.dowodyrejestracyjne.ui.start.scan

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.common.Barcode
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme
import wkolendo.dowodyrejestracyjne.utils.Screen
import wkolendo.dowodyrejestracyjne.utils.TrackScreen

/**
 * Scanner for the Aztec code printed on Polish vehicle registration certificates.
 *
 * Thin stateful wrapper: it only owns the camera state holder and feeds its output to the
 * stateless [ScannerDialogContent] below.
 */
@Composable
fun ScannerDialog(
    onBarcodeScanned: (Barcode) -> Unit,
    onDismissRequest: () -> Unit,
) {
    TrackScreen(Screen.SCANNER)

    val scannerState = rememberBarcodeScannerState()
    val lifecycleOwner = LocalLifecycleOwner.current
    // Keeps the effect from restarting just because the caller passed a new lambda instance.
    val currentOnBarcodeScanned by rememberUpdatedState(onBarcodeScanned)

    LaunchedEffect(scannerState, lifecycleOwner) {
        scannerState.bindAndScan(lifecycleOwner) { currentOnBarcodeScanned(it) }
    }

    ScannerDialogContent(
        surfaceRequest = scannerState.surfaceRequest,
        hasFlashUnit = scannerState.hasFlashUnit,
        isTorchOn = scannerState.isTorchOn,
        onTorchChange = scannerState::setTorchEnabled,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun ScannerDialogContent(
    surfaceRequest: SurfaceRequest?,
    hasFlashUnit: Boolean,
    isTorchOn: Boolean,
    onTorchChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = stringResource(R.string.camera_scanner_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.camera_scanner_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(Modifier.height(16.dp))

                // The old AlertDialog gave the preview match_parent, which starved the button panel
                // and left "cancel" with zero height. A weight keeps the button's space reserved.
                Viewfinder(
                    surfaceRequest = surfaceRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TorchButton(
                        hasFlashUnit = hasFlashUnit,
                        isTorchOn = isTorchOn,
                        onTorchChange = onTorchChange,
                    )
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            }
        }
    }
}

/**
 * Sits in the button bar rather than over the preview: it stays within thumb reach, needs no scrim
 * to stay legible against arbitrary camera output, and never covers part of the frame.
 */
@Composable
private fun TorchButton(
    hasFlashUnit: Boolean,
    isTorchOn: Boolean,
    onTorchChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = isTorchOn,
        onCheckedChange = onTorchChange,
        enabled = hasFlashUnit,
        colors = IconButtonDefaults.iconToggleButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            checkedContentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Icon(
            // The bolt glyph is narrower than the square icons, so it is sized explicitly to keep
            // the same optical weight as the rest.
            painter = painterResource(
                if (isTorchOn) R.drawable.ic_bolt_lightning_filled_24dp else R.drawable.ic_bolt_lightning_24dp
            ),
            contentDescription = stringResource(R.string.scanner_torch),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun Viewfinder(
    surfaceRequest: SurfaceRequest?,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Color.Black)) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
                // EXTERNAL (the default) renders into a SurfaceView, which lives in its own window
                // layer and therefore ignores the parent's rounded corners — the preview would
                // bleed past the card. EMBEDDED draws into a TextureView, which clips normally.
                implementationMode = ImplementationMode.EMBEDDED,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(name = "Skaner — bez podglądu", showBackground = true)
@Composable
private fun ScannerDialogContentPreview() {
    // A null surface request is exactly what the screen shows before the camera delivers frames,
    // which is why splitting the content out makes it previewable without a device.
    DRTheme(dynamicColor = false) {
        ScannerDialogContent(
            surfaceRequest = null,
            hasFlashUnit = true,
            isTorchOn = false,
            onTorchChange = {},
            onDismissRequest = {},
        )
    }
}
