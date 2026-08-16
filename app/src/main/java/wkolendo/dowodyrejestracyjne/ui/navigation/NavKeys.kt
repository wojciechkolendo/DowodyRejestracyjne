package wkolendo.dowodyrejestracyjne.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import wkolendo.dowodyrejestracyjne.models.Certificate

/**
 * Destinations of the app.
 *
 * In Navigation 3 a destination is just a value, and the back stack is a plain list of these. They
 * are serialisable so the stack survives process death.
 */

@Serializable
data object StartKey : NavKey

/**
 * Carries the whole certificate rather than an id, because a scan is shown even when the user
 * turned off saving — in that case there is no database row to look up.
 */
@Serializable
data class DetailsKey(val certificate: Certificate) : NavKey

@Serializable
data object SettingsKey : NavKey
