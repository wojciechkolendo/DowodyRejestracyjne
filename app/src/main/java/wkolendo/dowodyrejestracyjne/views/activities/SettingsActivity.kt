package wkolendo.dowodyrejestracyjne.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_fragment_holder.*
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.views.fragments.SettingsFragment

/**
 * @author Wojtek Kolendo
 */
class SettingsActivity : DowodyRejestracyjneActivity() {

	private val FRAGMENT_CONTAINER = R.id.fragmentContainer

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fragment_holder)
		initToolbar()

		if (savedInstanceState == null) {
			supportFragmentManager.transaction {
				add(FRAGMENT_CONTAINER, SettingsFragment())
			}
		}
	}

	private fun initToolbar() {
		toolbar.setNavigationIcon(R.drawable.all_back_grey_24dp)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		titleTextView.setText(R.string.settings_settings)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			onBackPressed()
			return true
		}
		return super.onOptionsItemSelected(item)
	}
}