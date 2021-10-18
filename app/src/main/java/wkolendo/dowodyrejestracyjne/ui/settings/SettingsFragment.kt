package wkolendo.dowodyrejestracyjne.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.repository.CertificateRepository
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.ui.BindingActivity

const val SETTINGS_SAVE_SCANS = "settings_save_scans"
const val SETTINGS_CLEAR_HISTORY = "settings_clear_history"
const val SETTINGS_CONTACT = "settings_contact"
const val SETTINGS_VERSION = "settings_version"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? BindingActivity<*>)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = R.string.settings_settings.getText()
        }
        initScannerSettings()
        initAboutSettings()
    }

    private fun initScannerSettings() {
        preferenceScreen.findPreference<Preference>(SETTINGS_CLEAR_HISTORY)?.setOnPreferenceClickListener {
            showClearHistoryDialog()
            true
        }
    }

    private fun initAboutSettings() {
        preferenceScreen.findPreference<Preference>(SETTINGS_CONTACT)?.setOnPreferenceClickListener { openEmailActivity() }
        preferenceScreen.findPreference<Preference>(SETTINGS_VERSION)?.apply {
            summary = getString(R.string.settings_app_version_summary, BuildConfig.VERSION_NAME)
            setOnPreferenceClickListener { openGooglePlayActivity() }
        }
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.settings_clear_history)
            .setMessage(R.string.settings_clear_history_message)
            .setNeutralButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.settings_clear_history_confirm) { _, _ -> CertificateRepository.deleteCertificates() }
            .show()
    }

    private fun openEmailActivity() = runCatching {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, Array(1) { getString(R.string.settings_contact_summary) })
        }, getString(R.string.settings_send_email)))
    }.onFailure { logError(it) }.isSuccess

    private fun openGooglePlayActivity() = runCatching {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)))
    }.recoverCatching {
        logError(it)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)))
    }.isSuccess
}