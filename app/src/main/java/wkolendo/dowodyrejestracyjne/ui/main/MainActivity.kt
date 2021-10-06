package wkolendo.dowodyrejestracyjne.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.ActivityMainBinding
import wkolendo.dowodyrejestracyjne.ui.start.StartFragment
import wkolendo.dowodyrejestracyjne.utils.ui.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) showStartFragment()
    }

    private fun showStartFragment() {
        openFragment(StartFragment(), addToBackStack = false)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}