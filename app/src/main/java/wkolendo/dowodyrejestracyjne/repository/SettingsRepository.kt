package wkolendo.dowodyrejestracyjne.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import wkolendo.dowodyrejestracyjne.appContext

/**
 * User settings, kept behind a repository so callers do not reach for SharedPreferences directly.
 *
 * A natural next step is DataStore, which would make reads suspend and drop the blocking main
 * thread access this class still has on creation.
 */
object SettingsRepository {

    private const val KEY_SAVE_SCANS = "settings_save_scans"

    /**
     * Deliberately the same file name PreferenceManager.getDefaultSharedPreferences() used, so the
     * setting survives dropping androidx.preference. Changing this string would silently reset
     * everyone's preferences on update.
     */
    private val preferences: SharedPreferences =
        appContext.getSharedPreferences("${appContext.packageName}_preferences", Context.MODE_PRIVATE)

    private val _saveScans = MutableStateFlow(preferences.getBoolean(KEY_SAVE_SCANS, true))
    val saveScans: StateFlow<Boolean> = _saveScans.asStateFlow()

    fun setSaveScans(enabled: Boolean) {
        preferences.edit { putBoolean(KEY_SAVE_SCANS, enabled) }
        _saveScans.value = enabled
    }
}
