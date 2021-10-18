package wkolendo.dowodyrejestracyjne.repository.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import wkolendo.dowodyrejestracyjne.appContext
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.repository.database.Migrations.INIT_DATABASE_VERSION

@Database(
    entities = [Certificate::class],
    version = INIT_DATABASE_VERSION,
    exportSchema = false,
)

internal abstract class AppDatabase : RoomDatabase() {
    abstract fun certificateDao(): CertificateDao
}

object Database {

    private val instance = Room.databaseBuilder(appContext, AppDatabase::class.java, "vehicle_certificates.db").build()

    val certificateDao: CertificateDao get() = instance.certificateDao()
}

object Migrations {

    const val INIT_DATABASE_VERSION = 1

// make Migration here if needed

}