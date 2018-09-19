package wkolendo.dowodyrejestracyjne.views.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import software.rsquared.androidlogger.Logger
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.repos.preferences.PreferenceHelper
import wkolendo.dowodyrejestracyjne.views.activities.DowodyRejestracyjneActivity

/**
 * @author Wojtek Kolendo
 */
class SettingsFragment : PreferenceFragmentCompat() {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.settings)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initDetectorSettings()
		initAboutSettings()
	}

	private fun initDetectorSettings() {
//		val clearPreference = preferenceScreen.findPreference(PreferenceHelper.SETTINGS_CLEAR_HISTORY_KEY)
//
//		clearPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
//			AlertDialog.Builder(requireContext(), R.style.Theme_DowodyRejestracyjne_Dialog)
//					.setCancelable(false)
//					.setTitle(R.string.settings_clear_history)
//					.setMessage(R.string.settings_clear_history_message)
//					.setPositiveButton(android.R.string.yes) { _, _ -> (activity as DowodyRejestracyjneActivity).showSnackMessage("not imeplemented") }
//					.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
//					.show()
//			true
//		}
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