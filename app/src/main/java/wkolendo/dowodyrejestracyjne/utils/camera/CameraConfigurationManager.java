/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wkolendo.dowodyrejestracyjne.utils.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import software.rsquared.androidlogger.Logger;


/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
@SuppressWarnings("deprecation") // camera APIs
final class CameraConfigurationManager {

	private final Context context;
	private int cwNeededRotation;
	private int cwRotationFromDisplayToCamera;
	private Point screenResolution;
	private Point cameraResolution;
	private Point bestPreviewSize;
	private Point previewSizeOnScreen;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(OpenCamera camera) {
		Camera.Parameters parameters = camera.getCamera().getParameters();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();

		int displayRotation = display.getRotation();
		int cwRotationFromNaturalToDisplay;
		switch (displayRotation) {
			case Surface.ROTATION_0:
				cwRotationFromNaturalToDisplay = 0;
				break;
			case Surface.ROTATION_90:
				cwRotationFromNaturalToDisplay = 90;
				break;
			case Surface.ROTATION_180:
				cwRotationFromNaturalToDisplay = 180;
				break;
			case Surface.ROTATION_270:
				cwRotationFromNaturalToDisplay = 270;
				break;
			default:
				// Have seen this return incorrect values like -90
				if (displayRotation % 90 == 0) {
					cwRotationFromNaturalToDisplay = (360 + displayRotation) % 360;
				} else {
					throw new IllegalArgumentException("Bad rotation: " + displayRotation);
				}
		}
		Logger.debug("Display at: " + cwRotationFromNaturalToDisplay);

		int cwRotationFromNaturalToCamera = camera.getOrientation();
		Logger.debug("Camera at: " + cwRotationFromNaturalToCamera);

		// Still not 100% sure about this. But acts like we need to flip this:
		if (camera.getFacing() == CameraFacing.FRONT) {
			cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360;
			Logger.debug("Front camera overriden to: " + cwRotationFromNaturalToCamera);
		}

    /*
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String overrideRotationString;
    if (camera.getFacing() == CameraFacing.FRONT) {
      overrideRotationString = prefs.getString(PreferencesActivity.KEY_FORCE_CAMERA_ORIENTATION_FRONT, null);
    } else {
      overrideRotationString = prefs.getString(PreferencesActivity.KEY_FORCE_CAMERA_ORIENTATION, null);
    }
    if (overrideRotationString != null && !"-".equals(overrideRotationString)) {
      Logger.debug("Overriding camera manually to " + overrideRotationString);
      cwRotationFromNaturalToCamera = Integer.parseInt(overrideRotationString);
    }
     */

		cwRotationFromDisplayToCamera =
				(360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360;
		Logger.debug("Final display orientation: " + cwRotationFromDisplayToCamera);
		if (camera.getFacing() == CameraFacing.FRONT) {
			Logger.debug("Compensating rotation for front camera");
			cwNeededRotation = (360 - cwRotationFromDisplayToCamera) % 360;
		} else {
			cwNeededRotation = cwRotationFromDisplayToCamera;
		}
		Logger.debug("Clockwise rotation from display to camera: " + cwNeededRotation);

		Point theScreenResolution = new Point();
		display.getSize(theScreenResolution);
		screenResolution = theScreenResolution;
		Logger.debug("Screen resolution in current orientation: " + screenResolution);
		cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Logger.debug("Camera resolution: " + cameraResolution);
		bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Logger.debug("Best available preview size: " + bestPreviewSize);

		boolean isScreenPortrait = screenResolution.x < screenResolution.y;
		boolean isPreviewSizePortrait = bestPreviewSize.x < bestPreviewSize.y;

		if (isScreenPortrait == isPreviewSizePortrait) {
			previewSizeOnScreen = bestPreviewSize;
		} else {
			previewSizeOnScreen = new Point(bestPreviewSize.y, bestPreviewSize.x);
		}
		Logger.debug("Preview size on screen: " + previewSizeOnScreen);
	}

	void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {

		Camera theCamera = camera.getCamera();
		Camera.Parameters parameters = theCamera.getParameters();

		if (parameters == null) {
			Logger.debug("Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		Logger.debug("Initial camera parameters: " + parameters.flatten());

		if (safeMode) {
			Logger.debug("In camera config safe mode -- most settings will not be honored");
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		CameraConfigurationUtils.setFocus(
				parameters,
				true,
				true,
				safeMode);

		parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);

		theCamera.setParameters(parameters);

		theCamera.setDisplayOrientation(cwRotationFromDisplayToCamera);

		Camera.Parameters afterParameters = theCamera.getParameters();
		Camera.Size afterSize = afterParameters.getPreviewSize();
		if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
			Logger.debug("Camera said it supported preview size " + bestPreviewSize.x + 'x' + bestPreviewSize.y +
					", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
			bestPreviewSize.x = afterSize.width;
			bestPreviewSize.y = afterSize.height;
		}
	}

	Point getBestPreviewSize() {
		return bestPreviewSize;
	}

	Point getPreviewSizeOnScreen() {
		return previewSizeOnScreen;
	}

	Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	int getCWNeededRotation() {
		return cwNeededRotation;
	}
}
