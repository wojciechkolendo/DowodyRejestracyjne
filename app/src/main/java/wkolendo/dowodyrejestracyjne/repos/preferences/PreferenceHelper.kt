package wkolendo.dowodyrejestracyjne.repos.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import wkolendo.dowodyrejestracyjne.DowodyRejestracyjneApplication

/**
 * @author Wojtek Kolendo
 */
object PreferenceHelper {

	val SETTINGS_CLEAR_HISTORY_KEY = "settings_clear_history"
	val SETTINGS_CONTACT_KEY = "settings_contact"
	val SETTINGS_VERSION_KEY = "settings_version"

	val prefs = DowodyRejestracyjneApplication.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
	val settingsPrefs = PreferenceManager.getDefaultSharedPreferences(DowodyRejestracyjneApplication.getContext())

//	fun isStartupPromptDisplayed(): Boolean {
//		return prefs.getBoolean(STARTUP_PROMP_DISPLAYED_KEY, false)
//	}
//
//	fun setStartupPromptDisplayed(displayed: Boolean) {
//		prefs.edit { putBoolean(STARTUP_PROMP_DISPLAYED_KEY, displayed) }
//	}
}