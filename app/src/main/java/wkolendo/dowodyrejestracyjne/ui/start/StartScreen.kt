package wkolendo.dowodyrejestracyjne.ui.start

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.start.scan.ScannerDialog
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme
import wkolendo.dowodyrejestracyjne.utils.Screen
import wkolendo.dowodyrejestracyjne.utils.TrackScreen
import wkolendo.dowodyrejestracyjne.utils.vibrateError
import wkolendo.dowodyrejestracyjne.utils.vibrateTap

/**
 * History of scanned certificates, with actions to open settings and start a new scan.
 *
 * Stateful wrapper: it wires the view model, the camera permission and the scanner dialog, then
 * hands plain values to [StartContent].
 */
@Composable
fun StartScreen(
    onOpenDetails: (Certificate) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel(),
) {
    TrackScreen(Screen.START)

    val context = LocalContext.current
    val certificates by viewModel.certificates.collectAsStateWithLifecycle()
    val isScannerVisible by viewModel.isScannerVisible.collectAsStateWithLifecycle()

    var errorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
    var showPermissionRationale by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) viewModel.showScanner() else showPermissionRationale = true
    }

    LaunchedEffect(viewModel) {
        viewModel.eventsFlow.collect { event ->
            when (event) {
                is StartViewModel.Event.OpenDetails -> onOpenDetails(event.certificate)
                is StartViewModel.Event.ShowError -> {
                    context.vibrateError()
                    errorMessage = event.textRes
                }
            }
        }
    }

    StartContent(
        certificates = certificates,
        onCertificateClick = viewModel::onCertificateClick,
        onSettingsClick = onOpenSettings,
        onScanClick = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                viewModel.showScanner()
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        modifier = modifier,
    )

    if (isScannerVisible) {
        ScannerDialog(
            onBarcodeScanned = { barcode ->
                context.vibrateTap()
                viewModel.onNewScan(barcode)
            },
            onDismissRequest = viewModel::hideScanner,
        )
    }

    errorMessage?.let { messageRes ->
        TrackScreen(Screen.DIALOG_SCAN_ERROR)
        MessageDialog(messageRes = messageRes, onDismiss = { errorMessage = null })
    }

    if (showPermissionRationale) {
        TrackScreen(Screen.DIALOG_CAMERA_PERMISSION)
        MessageDialog(
            messageRes = R.string.start_camera_permission_message,
            onDismiss = { showPermissionRationale = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartContent(
    certificates: List<Certificate>,
    onCertificateClick: (Certificate) -> Unit,
    onSettingsClick: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            if (certificates.isEmpty()) {
                EmptyState(
                    // Matches the 0.4 vertical bias the ConstraintLayout used: slightly above centre.
                    modifier = Modifier.align(BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f)),
                )
            } else {
                CertificateList(
                    certificates = certificates,
                    onCertificateClick = onCertificateClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            BottomActions(
                onSettingsClick = onSettingsClick,
                onScanClick = onScanClick,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun MessageDialog(@StringRes messageRes: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(stringResource(messageRes)) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_ok)) }
        },
    )
}

@Composable
private fun CertificateList(
    certificates: List<Certificate>,
    onCertificateClick: (Certificate) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        // Bottom padding keeps the last item clear of the floating action buttons.
        contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp),
    ) {
        items(certificates, key = { it.databaseId }) { certificate ->
            CertificateItem(
                certificate = certificate,
                onClick = { onCertificateClick(certificate) },
            )
        }
    }
}

@Composable
private fun CertificateItem(
    certificate: Certificate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_qrcode_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = certificate.vehicleRegistrationNumber.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_qrcode_thin_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .alpha(0.4f),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.start_empty_certificates_list),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BottomActions(
    onSettingsClick: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FloatingActionButton(
            onClick = onSettingsClick,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gear_24dp),
                contentDescription = stringResource(R.string.settings_settings),
            )
        }

        ExtendedFloatingActionButton(
            onClick = onScanClick,
            icon = {
                Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = null)
            },
            text = { Text(stringResource(R.string.start_scan_new)) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

private val sampleCertificates = listOf(
    Certificate(databaseId = 1, vehicleRegistrationNumber = "WA 1234A"),
    Certificate(databaseId = 2, vehicleRegistrationNumber = "KR 56789"),
    Certificate(databaseId = 3, vehicleRegistrationNumber = "GD 4321B"),
)

@Preview(name = "Lista", showBackground = true)
@Composable
private fun StartContentPreview() {
    DRTheme(dynamicColor = false) {
        StartContent(sampleCertificates, {}, {}, {})
    }
}

@Preview(name = "Pusta lista", showBackground = true)
@Composable
private fun StartContentEmptyPreview() {
    DRTheme(dynamicColor = false) {
        StartContent(emptyList(), {}, {}, {})
    }
}

@Preview(name = "Lista — ciemny", showBackground = true)
@Composable
private fun StartContentDarkPreview() {
    DRTheme(darkTheme = true, dynamicColor = false) {
        StartContent(sampleCertificates, {}, {}, {})
    }
}
