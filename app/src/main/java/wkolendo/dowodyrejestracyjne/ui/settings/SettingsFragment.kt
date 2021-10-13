package wkolendo.dowodyrejestracyjne.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.ui.BindingActivity

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
        initAboutSettings()
    }

    private fun initAboutSettings() {
        preferenceScreen.findPreference<Preference>("settings_contact")?.setOnPreferenceClickListener { openEmailActivity() }
        preferenceScreen.findPreference<Preference>("settings_version")?.apply {
            summary = getString(R.string.settings_app_version_summary, BuildConfig.VERSION_NAME)
            setOnPreferenceClickListener { openGooglePlayActivity() }
        }
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