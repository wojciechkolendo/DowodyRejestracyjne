@file:Suppress("unused")

package wkolendo.dowodyrejestracyjne.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import wkolendo.dowodyrejestracyjne.appContext

private const val VIBRATION_TAP: Long = 200

private const val VIBRATION_ERROR: Long = 1500

/**
 * Hides the soft keyboard
 */
fun Activity?.hideSoftKeyboard() = this?.currentFocus?.hideSoftKeyboard()

fun Context.showToastMessage(@StringRes msg: Int, duration: Int = Toast.LENGTH_SHORT): Toast = showToastMessage(msg.getText(), duration)

fun Context.showToastMessage(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast = Toast.makeText(this, msg, duration).apply { show() }

@get:Px
val Float?.dp: Int
    get() = appContext.dpToPx(this ?: 0f)

@get:Px
val Int?.dp: Int
    get() = appContext.dpToPx(this?.toFloat() ?: 0f)

@get:Px
val Double?.dp: Int
    get() = appContext.dpToPx(this?.toFloat() ?: 0f)

@get:Px
val Float?.sp: Int
    get() = appContext.spToPx(this ?: 0f)

@get:Px
val Int?.sp: Int
    get() = appContext.spToPx(this?.toFloat() ?: 0f)

@get:Px
val Double?.sp: Int
    get() = appContext.spToPx(this?.toFloat() ?: 0f)

val @receiver:ColorInt Int?.stateList: ColorStateList? get() = this?.let { ColorStateList.valueOf(it) }

private fun Context.dpToPx(value: Float): Int = (resources.displayMetrics.density * value).toInt()

private fun Context.spToPx(value: Float): Int = (resources.displayMetrics.scaledDensity * value).toInt()

fun @receiver:StringRes Int?.getText(): String =
    this?.let { appContext.getString(it) } ?: throw Resources.NotFoundException("Resource ID $this is not valid")

fun @receiver:StringRes Int?.getText(vararg formatArgs: Any?): String =
    this?.let { appContext.getString(it, *formatArgs) } ?: throw Resources.NotFoundException("Resource ID $this is not valid")

fun @receiver:StringRes Int?.getTextOrNull(): String? = this?.takeIf { it != 0 }?.runCatching { getText() }?.getOrNull()

fun @receiver:StringRes Int?.getTextOrNull(vararg formatArgs: Any?): String? = this?.takeIf { it != 0 }?.runCatching { getText(*formatArgs) }?.getOrNull()

@ColorInt
fun @receiver:ColorRes Int?.getColor(context: Context = appContext): Int =
    this?.let { ContextCompat.getColor(context, it) } ?: throw Resources.NotFoundException("Resource ID $this is not valid")

@ColorInt
fun @receiver:ColorRes Int?.getColorOrNull(context: Context = appContext): Int? = this?.takeIf { it != 0 }?.runCatching { getColor(context) }?.getOrNull()

@ColorInt
fun @receiver:AttrRes Int?.getAttrColor(context: Context): Int =
    this?.let { context.getColorFromAttr(it) } ?: throw Resources.NotFoundException("Resource ID $this is not valid")

@ColorInt
fun @receiver:AttrRes Int?.getAttrColorOrNull(context: Context): Int? =
    this?.takeIf { it != 0 }?.runCatching { getAttrColor(context) }?.getOrNull()

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun @receiver:DrawableRes Int?.getDrawable(context: Context = appContext): Drawable =
    this?.let { ContextCompat.getDrawable(context, it) } ?: throw Resources.NotFoundException("Resource ID $this is not valid")

fun @receiver:DrawableRes Int?.getDrawableOrNull(context: Context = appContext): Drawable? =
    this?.takeIf { it != 0 }?.runCatching { getDrawable(context) }?.getOrNull()

fun Context.vibrateTap() = vibrate(VIBRATION_TAP)

fun Context.vibrateError() = vibrate(VIBRATION_ERROR)

@Suppress("DEPRECATION")
fun Context.vibrate(millis: Long) {
    (getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) it.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
        else it.vibrate(millis)
    }
}

