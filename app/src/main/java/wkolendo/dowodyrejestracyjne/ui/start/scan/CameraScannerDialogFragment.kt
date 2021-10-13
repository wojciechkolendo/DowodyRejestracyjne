package wkolendo.dowodyrejestracyjne.ui.start.scan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.DialogScannerBinding
import wkolendo.dowodyrejestracyjne.ui.start.StartViewModel
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.ui.BindingDialogFragment
import wkolendo.dowodyrejestracyjne.utils.vibrateTap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class CameraScannerDialogFragment : BindingDialogFragment<DialogScannerBinding>(R.layout.dialog_scanner) {

    override val viewModel: StartViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val canReturnResult = AtomicBoolean(true)

    private val cameraExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    private var scanner: BarcodeScanner? = null

    override fun onBuildDialog(builder: AlertDialog.Builder) {
        builder.setTitle(R.string.camera_scanner_title)
        builder.setMessage(R.string.camera_scanner_message)
        builder.setNeutralButton(android.R.string.cancel, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_AZTEC).build())
    }

    override fun onDestroy() {
        super.onDestroy()
        scanner?.close()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(720, 1280))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().apply { setAnalyzer(cameraExecutor, ::onImage) }
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider) }
            runCatching {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, imageAnalysis, preview)
            }.onFailure(::logError)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun barcodesProcessed(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            if (barcode.format == Barcode.FORMAT_AZTEC && canReturnResult.compareAndSet(true, false)) barcodeFound(barcode)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun onImage(imageProxy: ImageProxy) {
        imageProxy.image?.also { mediaImage ->
            scanner?.process(InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees))
                ?.addOnSuccessListener(::barcodesProcessed)
                ?.addOnFailureListener(::logError)
                ?.addOnCompleteListener { imageProxy.close() }
                ?.addOnCanceledListener { imageProxy.close() }
        }
    }

    private fun barcodeFound(barcode: Barcode) {
        dismiss()
        viewModel.onNewScan(barcode)
        context?.vibrateTap()
    }
}
