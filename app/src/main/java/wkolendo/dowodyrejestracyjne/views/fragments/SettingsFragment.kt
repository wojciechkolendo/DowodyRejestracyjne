package wkolendo.dowodyrejestracyjne.views.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import software.rsquared.androidlogger.Logger
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.repos.preferences.PreferenceHelper

/**
 * @author Wojtek Kolendo
 */
class SettingsFragment : PreferenceFragmentCompat() {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.settings)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initAboutSettings()
	}

	private fun initAboutSettings() {
		val contactPreference = preferenceScreen.findPreference(PreferenceHelper.SETTINGS_CONTACT_KEY)
		val versionPreference = preferenceScreen.findPreference(PreferenceHelper.SETTINGS_VERSION_KEY)

		contactPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
				data = Uri.parse("mailto:")
				putExtra(Intent.EXTRA_EMAIL, Array(1) { getString(R.string.settings_contact_summary) })
			}, getString(R.string.settings_send_email)))
			true
		}

		versionPreference.summary = getString(R.string.settings_app_version_summary, BuildConfig.VERSION_NAME)
		versionPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
			try {
				startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)))
			} catch (e: ActivityNotFoundException) {
				Logger.error(e)
				startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)))
			}
			true
		}
	}

}