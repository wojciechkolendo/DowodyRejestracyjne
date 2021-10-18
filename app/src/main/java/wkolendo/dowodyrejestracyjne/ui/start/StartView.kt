package wkolendo.dowodyrejestracyjne.ui.start

import kotlinx.coroutines.flow.StateFlow
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.ui.OnItemClickListener

interface StartView {

    val certificates: StateFlow<List<Certificate>?>

    val onCertificateClick: OnItemClickListener<Certificate>
}