package wkolendo.dowodyrejestracyjne.ui.start

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.appContext
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.repository.CertificateRepository
import wkolendo.dowodyrejestracyjne.ui.settings.SETTINGS_SAVE_SCANS
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.scanner.Base64
import wkolendo.dowodyrejestracyjne.utils.scanner.NRV2EDecompressor
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel
import wkolendo.dowodyrejestracyjne.utils.ui.OnItemClickListener
import wkolendo.dowodyrejestracyjne.utils.ui.onItemClickListener

class StartViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), StartView {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    override val certificates: StateFlow<List<Certificate>?> = CertificateRepository.certificates
    override val onCertificateClick: OnItemClickListener<Certificate> = onItemClickListener(::openCertificate)

    fun onNewScan(barcode: Barcode) {
        runCatching { saveCertificate(String(NRV2EDecompressor.decompress(Base64.decode(barcode.rawValue)), Charsets.UTF_16LE).toCertificate()) }.onFailure {
            logError(it)
            viewModelScope.launch { eventChannel.send(Event.ShowError(R.string.camera_scanner_error)) }
        }
    }

    override fun onSaveState() = Unit

    private fun saveCertificate(certificate: Certificate) {
        if (PreferenceManager.getDefaultSharedPreferences(appContext)?.getBoolean(SETTINGS_SAVE_SCANS, true) == true) {
            CertificateRepository.insertCertificate(certificate)
        }
        openCertificate(certificate)
    }

    private fun openCertificate(certificate: Certificate) {
        viewModelScope.launch { eventChannel.send(Event.OpenDetails(certificate)) }
    }

    private fun String.toCertificate(): Certificate {
        logError(this)
        val data = this.split('|').onEachIndexed { index, s -> logError(index, s) }
        return Certificate(
            series = data[1],
            issuingAuthority = data.joinNotEmpty(range = 3..6),
            vehicleRegistrationNumber = data[7],
            vehicleManufacturer = data[8],
            vehicleType = data[9],
            vehicleTypeVariant = data[10],
            vehicleTypeVersion = data[11],
            vehicleModel = data[12],
            vehicleIdentificationNumber = data[13],
            issuingDate = data[14],
            expiryDate = data[15],

            certificateKeeperName = data.joinNotEmpty(range = 16..19),
            certificateKeeperId = data[20],
            certificateKeeperAddress = data.joinNotEmpty(range = 21..26, appendLine = false),

            vehicleOwnerName = data.joinNotEmpty(range = 27..30),
            vehicleOwnerId = data[31],
            vehicleOwnerAddress = data.joinNotEmpty(range = 32..37, appendLine = false),

            maxPermissibleWeight = data[38],
            maxAuthorisedWeight = data[39],
            maxTrainWeight = data[40],
            vehicleOwnWeight = data[41],
            vehicleCategory = data[42],
            typeApprovalNumber = data[43],
            numberOfAxles = data[44],
            maxBrakedTrailerMass = data[45],
            maxUnbrakedTrailerMass = data[46],
            powerWeightRatio = data[47],
            cylinderCapacity = data[48],
            maxNetPower = data[49],
            fuelType = data[50],
            dateOfFirstRegistration = data[51],
            numberOfSeats = data[52],
            numberOfStandingPlaces = data[53],

            vehicleClass = data[54],
            purpose = data[55],
            yearOfManufacture = data[56],
            maxPermissibleLoad = data[57],
            maxAxlePressure = data[58],
            vehicleCardId = data[59]
        )
    }

    private fun List<String>.joinNotEmpty(range: IntRange, appendLine: Boolean = true) = buildString {
        for (i in range) {
            this@joinNotEmpty[i].takeIf { it.isNotBlank() }?.also {
                if (appendLine && i != range.first) appendLine()
                append(it)
            }
        }
    }

    sealed class Event {
        data class OpenDetails(val certificate: Certificate) : Event()
        data class ShowError(@StringRes val textRes: Int) : Event()
    }
}