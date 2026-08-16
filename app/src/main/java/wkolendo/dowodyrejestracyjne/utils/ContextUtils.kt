package wkolendo.dowodyrejestracyjne.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

private const val VIBRATION_TAP: Long = 200

private const val VIBRATION_ERROR: Long = 1500

/** Short confirmation buzz, used when a code has been read successfully. */
fun Context.vibrateTap() = vibrate(VIBRATION_TAP)

/** Longer buzz for a failed scan. */
fun Context.vibrateError() = vibrate(VIBRATION_ERROR)

@Suppress("DEPRECATION")
private fun Context.vibrate(millis: Long) {
    (getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.vibrate(
        VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
    )
}
