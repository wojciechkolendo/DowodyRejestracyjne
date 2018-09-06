package wkolendo.dowodyrejestracyjne.views.activities

import android.Manifest
import android.os.Bundle
import software.rsquared.permissiontools.OnPermissionResultTask
import software.rsquared.permissiontools.Permissions
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.camera.CameraManager

/**
 * @author Wojtek Kolendo
 */
class CameraActivity : DowodyRejestracyjneActivity() {

	private var cameraManager: CameraManager? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_camera)
	}

	override fun onResume() {
		super.onResume()
		checkCameraPermission()
	}

	private fun checkCameraPermission() {
		Permissions.checkPermission(this, object : OnPermissionResultTask() {
			@Throws(SecurityException::class)
			override fun onPermissionGranted(permissions: Array<String>) {
				initCamera()
			}
		}, Manifest.permission.CAMERA)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private fun initCamera() {

	}
}