package wkolendo.dowodyrejestracyjne.ui.start.scan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.libraries.barhopper.RecognitionOptions.AZTEC
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.DialogScannerBinding
import wkolendo.dowodyrejestracyjne.ui.start.StartViewModel
import wkolendo.dowodyrejestracyjne.utils.logDebug
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.ui.BindingDialogFragment
import wkolendo.dowodyrejestracyjne.utils.vibrateTap
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class CameraScannerDialogFragment : BindingDialogFragment<DialogScannerBinding>(R.layout.dialog_scanner), CameraXConfig.Provider {

    override val viewModel: StartViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val canReturnResult = AtomicBoolean(true)

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC)
        .build()

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onBuildDialog(builder: AlertDialog.Builder) {
        builder.setTitle("Umieść środek kodu na linii")
        builder.setNeutralButton(android.R.string.cancel, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding?.scannerView?.apply {
//            decoderFactory = AztecDecoderFactory()
//            decodeContinuous(this@CameraScannerDialogFragment)
//            if (isActivated) resume()
//        }
        startCamera()
    }

    private fun startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { image ->
                onImage(image)
            })

            // Preview
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider) }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, imageAnalysis, preview)

            } catch (exc: Exception) {
                logError("Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun barcodesProcessed(barcodes : List<Barcode>) {
        for (barcode in barcodes) {
            val bounds = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue

            barcodeFound(barcode)

            val valueType = barcode.valueType
            // See API reference for complete list of supported types
            when (valueType) {
                Barcode.TYPE_WIFI -> {
                    val ssid = barcode.wifi!!.ssid
                    val password = barcode.wifi!!.password
                    val type = barcode.wifi!!.encryptionType
                }
                Barcode.TYPE_URL -> {
                    val title = barcode.url!!.title
                    val url = barcode.url!!.url
                }
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun onImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // ...
                    barcodesProcessed(barcodes)
                }
                .addOnFailureListener {
                    logError(it)
                    // Task failed with an exception
                    // ...
                }
        }
        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()
//        logError(luma)
        imageProxy.close()
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    private fun barcodeFound(barcode: Barcode) {
        logError("${barcode.format} ${barcode.rawValue}")
        if (barcode.format == Barcode.FORMAT_AZTEC && canReturnResult.compareAndSet(true, false)) {
            dismiss()
            context?.vibrateTap()
            viewModel.onNewScan(barcode)
        }
    }
}
