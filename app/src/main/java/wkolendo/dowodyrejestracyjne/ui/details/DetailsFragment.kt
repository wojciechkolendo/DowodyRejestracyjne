package wkolendo.dowodyrejestracyjne.ui.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.FragmentDetailsBinding
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.showSnackMessage
import wkolendo.dowodyrejestracyjne.utils.ui.BindingFragment

class DetailsFragment : BindingFragment<FragmentDetailsBinding>(R.layout.fragment_details) {

    override val viewModel: DetailsViewModel by viewModels()

    override val toolbarHomeAsUp = true

    companion object {
        fun newInstance(certificate: Certificate) = DetailsFragment().apply { arguments = bundleOf(EXTRA_CERTIFICATE to certificate) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.certificate.observe { it?.vehicleRegistrationNumber?.also { regNumber -> toolbarTitle = regNumber } }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_copy -> {
            copyCertificateData()
            true
        }
        R.id.action_share -> {
            shareCertificateData()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun copyCertificateData() {
        (activity?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
            ClipData.newPlainText("label", viewModel.buildCertificateString())
        )
        showSnackMessage(R.string.details_copy_success_message)
    }

    private fun shareCertificateData() = startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, viewModel.buildCertificateString())
    }, R.string.details_share.getText()))
}