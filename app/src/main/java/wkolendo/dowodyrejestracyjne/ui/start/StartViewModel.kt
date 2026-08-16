package wkolendo.dowodyrejestracyjne.ui.start

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.models.toCertificate
import wkolendo.dowodyrejestracyjne.repository.CertificateRepository
import wkolendo.dowodyrejestracyjne.repository.SettingsRepository
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.scanner.Base64
import wkolendo.dowodyrejestracyjne.utils.scanner.NRV2EDecompressor

class StartViewModel : ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val certificates: StateFlow<List<Certificate>> = CertificateRepository.certificates

    private val _isScannerVisible = MutableStateFlow(false)
    val isScannerVisible: StateFlow<Boolean> = _isScannerVisible.asStateFlow()

    fun onCertificateClick(certificate: Certificate) = openCertificate(certificate)

    fun showScanner() {
        _isScannerVisible.value = true
    }

    fun hideScanner() {
        _isScannerVisible.value = false
    }

    fun onNewScan(barcode: Barcode) {
        hideScanner()
        runCatching { saveCertificate(String(NRV2EDecompressor.decompress(Base64.decode(barcode.rawValue)), Charsets.UTF_16LE).toCertificate()) }.onFailure {
            logError(it)
            viewModelScope.launch { eventChannel.send(Event.ShowError(R.string.camera_scanner_error)) }
        }
    }

    private fun saveCertificate(certificate: Certificate) {
        if (SettingsRepository.saveScans.value) CertificateRepository.insertCertificate(certificate)
        openCertificate(certificate)
    }

    private fun openCertificate(certificate: Certificate) {
        viewModelScope.launch { eventChannel.send(Event.OpenDetails(certificate)) }
    }

    sealed class Event {
        data class OpenDetails(val certificate: Certificate) : Event()
        data class ShowError(@StringRes val textRes: Int) : Event()
    }
}