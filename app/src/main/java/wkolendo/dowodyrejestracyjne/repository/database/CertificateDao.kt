package wkolendo.dowodyrejestracyjne.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import wkolendo.dowodyrejestracyjne.models.Certificate

@Dao
interface CertificateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(certificate: Certificate)

    @Query("DELETE FROM certificates")
    suspend fun deleteAll()

    @Query("SELECT * FROM certificates")
    fun loadAll(): Flow<List<Certificate>>

}