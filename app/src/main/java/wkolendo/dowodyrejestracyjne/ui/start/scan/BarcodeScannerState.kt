package wkolendo.dowodyrejestracyjne.ui.start.scan

import android.annotation.SuppressLint
import android.content.Context
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.withContext
import wkolendo.dowodyrejestracyjne.utils.logError
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Owns the camera and the ML Kit detector for the scanner screen.
 *
 * This is a plain state holder rather than a `ViewModel` on purpose: the camera must live exactly
 * as long as the preview is on screen, while a `ViewModel` deliberately outlives it. Keeping it
 * here also leaves the composables free of camera plumbing, so they stay previewable.
 *
 * Create it with [rememberBarcodeScannerState] and drive it from a `LaunchedEffect`.
 */
@Stable
class BarcodeScannerState(private val context: Context) {

    /** Set once the camera has a frame producer ready; the viewfinder renders nothing until then. */
    var surfaceRequest by mutableStateOf<SurfaceRequest?>(null)
        private set

    /**
     * Binds the camera and reports the first Aztec code found. Never returns normally — cancel the
     * calling coroutine (which happens when the caller leaves composition) to release everything.
     */
    suspend fun bindAndScan(lifecycleOwner: LifecycleOwner, onBarcodeScanned: (Barcode) -> Unit) {
        // The analyzer runs on a background thread, so the one-shot guard has to be atomic.
        val canReturnResult = AtomicBoolean(true)
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_AZTEC).build()
        )
        val analysisExecutor = Executors.newSingleThreadExecutor()

        // Getting the provider can fail on devices with a broken or busy camera stack. Without this
        // the exception escapes the effect's coroutine and takes the app down.
        val cameraProvider = withContext(Dispatchers.IO) {
            runCatching { ProcessCameraProvider.getInstance(context).get() }.getOrNull()
        }
        if (cameraProvider == null) {
            logError(IllegalStateException("Camera provider unavailable"))
            analysisExecutor.shutdown()
            scanner.close()
            return
        }

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider { request -> surfaceRequest = request }
        }

        val imageAnalysis = ImageAnalysis.Builder()
            // Aztec codes on the certificate are dense, so keep the analysis frames reasonably
            // large. This replaces the deprecated setTargetResolution(Size(720, 1280)).
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setResolutionStrategy(
                        ResolutionStrategy(
                            Size(720, 1280),
                            ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER,
                        )
                    )
                    .build()
            )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(analysisExecutor) { imageProxy ->
                    imageProxy.scanForAztec(scanner) { barcode ->
                        if (canReturnResult.compareAndSet(true, false)) onBarcodeScanned(barcode)
                    }
                }
            }

        try {
            cameraProvider.unbindAll()
            runCatching {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageAnalysis,
                    preview,
                )
            }.onFailure { logError(it) }
            awaitCancellation()
        } finally {
            cameraProvider.unbindAll()
            imageAnalysis.clearAnalyzer()
            scanner.close()
            analysisExecutor.shutdown()
            surfaceRequest = null
        }
    }
}

@Composable
fun rememberBarcodeScannerState(): BarcodeScannerState {
    val context = LocalContext.current
    return remember(context) { BarcodeScannerState(context) }
}

/** Runs the detector over one frame and always closes the proxy, otherwise analysis stalls. */
@SuppressLint("UnsafeOptInUsageError")
private fun ImageProxy.scanForAztec(scanner: BarcodeScanner, onFound: (Barcode) -> Unit) {
    val mediaImage = image
    if (mediaImage == null) {
        close()
        return
    }
    scanner.process(InputImage.fromMediaImage(mediaImage, imageInfo.rotationDegrees))
        .addOnSuccessListener { barcodes ->
            barcodes.firstOrNull { it.format == Barcode.FORMAT_AZTEC }?.also(onFound)
        }
        .addOnFailureListener { logError(it) }
        .addOnCompleteListener { close() }
}
