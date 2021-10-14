package wkolendo.dowodyrejestracyjne.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.repository.database.CertificateDao
import wkolendo.dowodyrejestracyjne.repository.database.Database

object CertificateRepository {

    private val certificateDao: CertificateDao = Database.certificateDao
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    val certificates: StateFlow<List<Certificate>> = certificateDao.loadAll().stateIn(repositoryScope, SharingStarted.Eagerly, emptyList())

    fun insertCertificate(certificate: Certificate) {
        repositoryScope.launch { certificateDao.insert(certificate) }
    }
}