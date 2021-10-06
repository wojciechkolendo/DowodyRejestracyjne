package wkolendo.dowodyrejestracyjne.ui.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.FragmentStartBinding
import wkolendo.dowodyrejestracyjne.ui.settings.SettingsFragment
import wkolendo.dowodyrejestracyjne.utils.ui.BindingFragment

class StartFragment : BindingFragment<FragmentStartBinding>(R.layout.fragment_start) {

    override val toolbarHomeAsUp = false

    override val viewModel: StartViewModel by viewModels()

    override var toolbarTitle = "Czytnik DR"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.settings?.setOnClickListener { openSettingsFragment() }
    }

    private fun openSettingsFragment() = openFragment(SettingsFragment())
}