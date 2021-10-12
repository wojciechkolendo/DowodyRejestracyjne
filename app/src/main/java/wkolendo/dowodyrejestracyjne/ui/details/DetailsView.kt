package wkolendo.dowodyrejestracyjne.ui.details

import androidx.lifecycle.LiveData
import wkolendo.dowodyrejestracyjne.models.Certificate

interface DetailsView {

    val certificate: LiveData<Certificate?>
}