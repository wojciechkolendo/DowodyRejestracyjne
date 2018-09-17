package wkolendo.dowodyrejestracyjne.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_fragment_holder.*
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.views.fragments.ResultFragment

/**
 * @author Wojtek Kolendo
 */
class ResultActivity : DowodyRejestracyjneActivity() {

	private val FRAGMENT_CONTAINER = R.id.fragmentContainer
	private val EXTRA_TITLE = "extra_title"

	companion object {
		/**
		 * Plain text received from AZTEC code
		 */
		val EXTRA_RESULT = "extra_result"
	}

	private var title: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fragment_holder)

		if (savedInstanceState == null) {
			val result: String = intent.getStringExtra(EXTRA_RESULT)
			title = getNumberPlate(result)
			supportFragmentManager.transaction {
				add(FRAGMENT_CONTAINER, ResultFragment.newInstance(result))
			}
		} else {
			title = savedInstanceState.getString(EXTRA_TITLE)
		}

		initToolbar()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		outState?.putString(EXTRA_TITLE, title)
	}

	private fun initToolbar() {
		toolbar.setNavigationIcon(R.drawable.all_back_grey_24dp)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		title?.let { titleTextView.text = title }
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			onBackPressed()
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	private fun getNumberPlate(result: String): String? {
		var separatorIndex = 0
		for (i in 0 until result.length) {
			if (result[i] == '|') {
				separatorIndex++
			}
			if (separatorIndex == 7) {
				return result.substring(i + 1).substringBefore('|')
			}
		}
		return null
	}
}