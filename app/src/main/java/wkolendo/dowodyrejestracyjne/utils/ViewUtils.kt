@file:Suppress("HasPlatformType")

package wkolendo.dowodyrejestracyjne.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Hides the soft keyboard
 */
fun View.hideSoftKeyboard(clearFocus: Boolean = true, flags: Int = 0) {
    if (clearFocus) clearFocus()
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, flags)
}

/**
 * Requests focus and shows the soft keyboard
 */
fun View.showSoftKeyboard(flags: Int = 0) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    inputMethodManager.showSoftInput(this, flags)
}

inline fun <reified T : RecyclerView.Adapter<*>> RecyclerView.getOrInstantiateAdapter(newInstance: (() -> T) = { T::class.java.newInstance() }): T =
    (adapter as? T) ?: newInstance().apply { adapter = this }