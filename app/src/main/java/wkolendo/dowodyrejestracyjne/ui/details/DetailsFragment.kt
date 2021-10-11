package wkolendo.dowodyrejestracyjne.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.FragmentDetailsBinding
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.ui.BindingFragment

class DetailsFragment : BindingFragment<FragmentDetailsBinding>(R.layout.fragment_details) {

    override val viewModel: DetailsViewModel by viewModels()

    override val toolbarHomeAsUp = true

    companion object {
        fun newInstance(certificate: Certificate) = DetailsFragment().apply { arguments = bundleOf(EXTRA_CERTIFICATE to certificate) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.certificate.observe { it?.vehicleRegistrationNumber?.also { regNumber -> toolbarTitle = regNumber } }
    }
}