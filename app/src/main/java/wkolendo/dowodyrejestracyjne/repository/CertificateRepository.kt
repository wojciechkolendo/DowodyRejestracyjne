package wkolendo.dowodyrejestracyjne.repository

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.repository.database.CertificateDao
import wkolendo.dowodyrejestracyjne.repository.database.Database
import wkolendo.dowodyrejestracyjne.utils.logError

object CertificateRepository {

    private val certificateDao: CertificateDao = Database.certificateDao

    /**
     * A database failure used to escape the plain `launch` below and take the process down. The
     * handler turns it into a reported non-fatal instead, and the supervisor job keeps one failed
     * write from cancelling the scope that also feeds [certificates].
     */
    private val repositoryScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, throwable -> logError(throwable) }
    )

    val certificates: StateFlow<List<Certificate>> = certificateDao.loadAll()
        .catch { throwable ->
            logError(throwable)
            emit(emptyList())
        }
        .stateIn(repositoryScope, SharingStarted.Eagerly, emptyList())

    fun insertCertificate(certificate: Certificate) {
        repositoryScope.launch { certificateDao.insert(certificate) }
    }

    fun deleteCertificates() {
        repositoryScope.launch { certificateDao.deleteAll() }
    }
}
