package wkolendo.dowodyrejestracyjne.views.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_camera.*
import software.rsquared.androidlogger.Logger
import software.rsquared.permissiontools.OnPermissionResultTask
import software.rsquared.permissiontools.Permissions
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.camera.CameraManager
import wkolendo.dowodyrejestracyjne.utils.decoding.Base64
import wkolendo.dowodyrejestracyjne.utils.decoding.NRV2EDecompressor
import wkolendo.dowodyrejestracyjne.views.custom.ViewfinderView
import wkolendo.dowodyrejestracyjne.views.utils.CaptureActivityHandler

/**
 * @author Wojtek Kolendo
 */
class CameraActivity : DowodyRejestracyjneActivity(), SurfaceHolder.Callback {

	private val RESULT_PREFIX = "\ufeff"

	lateinit var cameraManager: CameraManager
	var handler: CaptureActivityHandler? = null
	private var hasSurface = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_camera)
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		closeImageView.setOnClickListener { onCloseClicked() }
		moreImageView.setOnClickListener { onMoreClicked() }
	}

	override fun onResume() {
		super.onResume()

		cameraManager = CameraManager(this)
		viewfinderView.setCameraManager(cameraManager)
		handler = null

		if (hasSurface) {
			initCamera(surfaceView.holder)
		} else {
			surfaceView.holder.addCallback(this)
		}
	}

	override fun onPause() {
		handler?.let {
			it.quitSynchronously()
			handler = null
		}
		if (!hasSurface) {
			surfaceView.holder.removeCallback(this)
		}
		super.onPause()
	}

	fun getViewfinderView(): ViewfinderView {
		return viewfinderView
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private fun initCamera(surfaceHolder: SurfaceHolder?) {
		Permissions.checkPermission(this, object : OnPermissionResultTask() {
			@Throws(SecurityException::class)
			override fun onPermissionGranted(permissions: Array<String>) {
				if (surfaceHolder == null) {
					throw IllegalStateException("No SurfaceHolder provided")
				}
				cameraManager.openDriver(surfaceHolder)
				if (handler == null) {
					handler = CaptureActivityHandler(this@CameraActivity, cameraManager)
				}
			}
		}, Manifest.permission.CAMERA)
	}

	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param rawResult The contents of the barcode.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param barcode   A greyscale bitmap of the camera data which was decoded.
	 */
	fun handleDecode(rawResult: Result, barcode: Bitmap, scaleFactor: Float) {
		if (rawResult.barcodeFormat == BarcodeFormat.AZTEC && rawResult.text.startsWith(RESULT_PREFIX)) {
			try {
				val debased = Base64.decode(rawResult.text.substring(1))
				val decompress = NRV2EDecompressor.decompress(debased)
				val text = String(decompress, Charsets.UTF_8)
				Logger.error(text)
				Toast.makeText(this, text, Toast.LENGTH_LONG).show()
			} catch (e: Exception) {
				Logger.error(e)
			}
		} else {
			Logger.error(rawResult.barcodeFormat, rawResult.text)
			Toast.makeText(this, rawResult.text, Toast.LENGTH_LONG).show()
		}
	}

	private fun onCloseClicked() {

	}

	private fun onMoreClicked() {
		startActivity(Intent(this, SettingsActivity::class.java))
	}

	fun drawViewfinder() {
		viewfinderView.drawViewfinder()
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//		nothing to do
	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		hasSurface = false
	}

	override fun surfaceCreated(holder: SurfaceHolder) {
		if (!hasSurface) {
			hasSurface = true
			initCamera(holder)
		}
	}
}