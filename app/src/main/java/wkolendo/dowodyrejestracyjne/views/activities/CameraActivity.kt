//package wkolendo.dowodyrejestracyjne.views.activities
//
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.os.VibrationEffect
//import android.os.Vibrator
//import android.view.SurfaceHolder
//import android.view.WindowManager
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.Result
//import kotlinx.android.synthetic.main.activity_camera.*
//import software.rsquared.androidlogger.Logger
//import software.rsquared.permissiontools.OnPermissionResultTask
//import software.rsquared.permissiontools.Permissions
//import wkolendo.dowodyrejestracyjne.R
//import wkolendo.dowodyrejestracyjne.utils.camera.CameraManager
//import wkolendo.dowodyrejestracyjne.utils.decoding.Base64
//import wkolendo.dowodyrejestracyjne.utils.decoding.NRV2EDecompressor
//import wkolendo.dowodyrejestracyjne.views.utils.CaptureActivityHandler
//
///**
// * @author Wojtek Kolendo
// */
//class CameraActivity : DowodyRejestracyjneActivity(), SurfaceHolder.Callback {
//
//	lateinit var cameraManager: CameraManager
//	var handler: CaptureActivityHandler? = null
//	private var hasSurface = false
//
//	override fun onCreate(savedInstanceState: Bundle?) {
//		super.onCreate(savedInstanceState)
//		setContentView(R.layout.activity_camera)
//		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//		closeImageView.setOnClickListener { onCloseClicked() }
//		moreImageView.setOnClickListener { onMoreClicked() }
//	}
//
//	override fun onResume() {
//		super.onResume()
//
//		cameraManager = CameraManager(this)
//		viewfinderView.setCameraManager(cameraManager)
//		handler = null
//
//		if (hasSurface) {
//			initCamera(surfaceView.holder)
//		} else {
//			surfaceView.holder.addCallback(this)
//		}
//	}
//
//	override fun onPause() {
//		handler?.let {
//			it.quitSynchronously()
//			handler = null
//		}
//		if (!hasSurface) {
//			surfaceView.holder.removeCallback(this)
//		}
//		super.onPause()
//	}
//
//	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//		Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
//	}
//
//	private fun initCamera(surfaceHolder: SurfaceHolder?) {
//		Permissions.checkPermission(this, object : OnPermissionResultTask() {
//			@Throws(SecurityException::class)
//			override fun onPermissionGranted(permissions: Array<String>) {
//				if (surfaceHolder == null) {
//					throw IllegalStateException("No SurfaceHolder provided")
//				}
//				cameraManager.openDriver(surfaceHolder)
//				if (handler == null) {
//					handler = CaptureActivityHandler(this@CameraActivity, cameraManager)
//				}
//			}
//		}, Manifest.permission.CAMERA)
//	}
//
//	/**
//	 * A valid barcode has been found, so give an indication of success and show the results.
//	 *
//	 * @param rawResult The contents of the barcode.
//	 */
//	fun handleDecode(rawResult: Result) {
//		if (rawResult.barcodeFormat == BarcodeFormat.AZTEC) {
//			try {
//				val debased = Base64.decode(rawResult.text)
//				val decompress = NRV2EDecompressor.decompress(debased)
//				val text = String(decompress, Charsets.UTF_16LE)
//
//				vibrate()
//				startActivity(Intent(this, ResultActivity::class.java).apply {
//					putExtra(ResultActivity.EXTRA_RESULT, text)
//				})
//			} catch (e: Exception) {
//				Logger.error(e)
//				showSnackMessage(R.string.camera_error)
//			}
//		}
//	}
//
//	private fun vibrate() {
//		val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
//		} else {
//			vibrator.vibrate(1000)
//		}
//	}
//
//	private fun onCloseClicked() {
//		System.exit(0)
//	}
//
//	private fun onMoreClicked() {
//		startActivity(Intent(this, SettingsActivity::class.java))
//	}
//
//	fun drawViewfinder() {
//		viewfinderView.drawViewfinder()
//	}
//
//	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
////		nothing to do
//	}
//
//	override fun surfaceDestroyed(holder: SurfaceHolder) {
//		hasSurface = false
//	}
//
//	override fun surfaceCreated(holder: SurfaceHolder) {
//		if (!hasSurface) {
//			hasSurface = true
//			initCamera(holder)
//		}
//	}
//}