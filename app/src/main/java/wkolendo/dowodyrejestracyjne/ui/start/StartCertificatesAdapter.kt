package wkolendo.dowodyrejestracyjne.ui.start

import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.ItemCertificateBinding
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.ui.BindingListAdapter
import wkolendo.dowodyrejestracyjne.utils.ui.diffCallback
import wkolendo.dowodyrejestracyjne.utils.ui.hasSame

class StartCertificatesAdapter : BindingListAdapter<Certificate, ItemCertificateBinding>(diffCallback, R.layout.item_certificate)

private val diffCallback = diffCallback<Certificate>(
    item = { hasSame(Certificate::databaseId) },
    content = { hasSame(Certificate::vehicleRegistrationNumber) }
)