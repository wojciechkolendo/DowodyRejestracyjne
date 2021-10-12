package wkolendo.dowodyrejestracyjne.ui.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel

internal const val EXTRA_CERTIFICATE = "extra_certificate"

class DetailsViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), DetailsView {

    override val certificate: LiveData<Certificate?> = state.getLiveData(EXTRA_CERTIFICATE)

    override fun onSaveState() = Unit

}