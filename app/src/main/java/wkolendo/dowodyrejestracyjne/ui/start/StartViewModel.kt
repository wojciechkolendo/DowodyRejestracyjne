package wkolendo.dowodyrejestracyjne.ui.start

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.scanner.Base64
import wkolendo.dowodyrejestracyjne.utils.scanner.NRV2EDecompressor
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel

class StartViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), StartView {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun onNewScan(barcode: Barcode) {
        runCatching { saveCertificate(String(NRV2EDecompressor.decompress(Base64.decode(barcode.rawValue)), Charsets.UTF_16LE).toCertificate()) }.onFailure {
            logError(it)
            viewModelScope.launch { eventChannel.send(Event.ShowError(R.string.camera_scanner_error)) }
        }
    }

    override fun onSaveState() = Unit

    private fun saveCertificate(certificate: Certificate) {
        viewModelScope.launch { eventChannel.send(Event.OpenDetails(certificate)) }
    }

    private fun String.toCertificate(): Certificate {
        logError(this)
        val data = this.split('|').onEachIndexed { index, s -> logError(index, s) }
        return Certificate(
            series = data[1],
            issuingAuthority = "${data[3]}\n${data[4]}\n${data[5]}\n${data[6]}",
            vehicleRegistrationNumber = data[7],
            vehicleManufacturer = data[8],
            vehicleType = data[9],
            vehicleTypeVariant = data[10],
            vehicleTypeVersion = data[11],
            vehicleModel = data[12],
            vehicleIdentificationNumber = data[13],
            issuingDate = data[14],
            expiryDate = data[15],

            certificateKeeperName = "${data[16]} ${data[17]} ${data[18]} ${data[19]}",
            certificateKeeperId = data[20],
            certificateKeeperAddress = "${data[21]} ${data[22]} ${data[23]} ${data[24]} ${data[25]} ${data[26]}",

            vehicleOwnerName = "${data[27]} ${data[28]} ${data[29]} ${data[30]}",
            vehicleOwnerId = data[31],
            vehicleOwnerAddress = "${data[32]} ${data[33]} ${data[34]} ${data[35]} ${data[36]} ${data[37]}",

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

    sealed class Event {
        data class OpenDetails(val certificate: Certificate) : Event()
        data class ShowError(@StringRes val textRes: Int) : Event()
    }
}