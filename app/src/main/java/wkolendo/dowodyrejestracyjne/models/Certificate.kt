package wkolendo.dowodyrejestracyjne.models

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import wkolendo.dowodyrejestracyjne.R

// @Serializable is what lets a certificate ride inside a Navigation 3 key, which is how the details
// screen receives it — including scans the user chose not to persist.
@Serializable
@Parcelize
@Entity(tableName = "certificates")
data class Certificate(
    @PrimaryKey(autoGenerate = true)
    val databaseId : Int = 0,
//    SERIA
    val series: String? = null,
//    ORGAN WYDAJĄCY
    val issuingAuthority: String? = null,
//    A — numer rejestracyjny pojazdu
    val vehicleRegistrationNumber: String? = null,
//    B — data pierwszej rejestracji pojazdu
    val dateOfFirstRegistration: String? = null,
//    C.1.1 — nazwisko lub nazwa posiadacza dowodu rejestracyjnego
    val certificateKeeperName: String? = null,
//    C.1.2 — numer PESEL lub REGON posiadacza dowodu rejestracyjnego
    val certificateKeeperId: String? = null,
//    C.1.3 — adres posiadacza dowodu rejestracyjnego
    val certificateKeeperAddress: String? = null,
//    C.2.1 — nazwisko lub nazwa właściciela pojazdu
    val vehicleOwnerName: String? = null,
//    C.2.2 — numer PESEL lub REGON właściciela pojazdu
    val vehicleOwnerId: String? = null,
//    C.2.3 — adres właściciela pojazdu
    val vehicleOwnerAddress: String? = null,
//    D.1 — marka pojazdu
    val vehicleManufacturer: String? = null,
//    D.2 — typ pojazdu
    val vehicleType: String? = null,
//    D.2 — wariant, jeżeli występuje
    val vehicleTypeVariant: String? = null,
//    D.2 — wersja, jeżeli występuje
    val vehicleTypeVersion: String? = null,
//    D.3 — model pojazdu
    val vehicleModel: String? = null,
//    E — numer identyfikacyjny pojazdu (numer VIN albo numer nadwozia, podwozia lub ramy)
    val vehicleIdentificationNumber: String? = null,
//    F.1 — maksymalna masa całkowita pojazdu, wyłączając motocykle i motorowery (w kg)
    val maxPermissibleWeight: String? = null,
//    F.2 — dopuszczalna masa całkowita pojazdu (w kg)
    val maxAuthorisedWeight: String? = null,
//    F.3 — dopuszczalna masa całkowita zespołu pojazdów (w kg)
    val maxTrainWeight: String? = null,
//    G — masa własna pojazdu; w przypadku pojazdu ciągnącego innego niż kategoria M1 masa własna pojazdu obejmuje urządzenie sprzęgające (w kg)
    val vehicleOwnWeight: String? = null,
//    H — okres ważności dowodu, jeżeli występuje takie ograniczenie
    val expiryDate: String? = null,
//    I — data wydania dowodu rejestracyjnego
    val issuingDate: String? = null,
//    J — kategoria pojazdu
    val vehicleCategory: String? = null,
//    K — numer świadectwa homologacji typu pojazdu, jeżeli występuje
    val typeApprovalNumber: String? = null,
//    L — liczba osi
    val numberOfAxles: String? = null,
//    O.1 — maksymalna masa całkowita przyczepy z hamulcem (w kg)
    val maxBrakedTrailerMass: String? = null,
//    O.2 — maksymalna masa całkowita przyczepy bez hamulca (w kg)
    val maxUnbrakedTrailerMass: String? = null,
//    P.1 — pojemność silnika (w cm³)
    val cylinderCapacity: String? = null,
//    P.2 — maksymalna moc netto silnika (w kW)
    val maxNetPower: String? = null,
//    P.3 — rodzaj paliwa
    val fuelType: String? = null,
//    Q — stosunek mocy do masy własnej (w kW/kg); dotyczy motocykli i motorowerów
    val powerWeightRatio: String? = null,
//    S.1 — liczba miejsc siedzących, włączając siedzenie kierowcy
    val numberOfSeats: String? = null,
//    S.2 — liczba miejsc stojących, jeżeli występuje
    val numberOfStandingPlaces: String? = null,
//    RODZAJ POJAZDU
    val vehicleClass: String? = null,
//    PRZEZNACZENIE
    val purpose: String? = null,
//    ROK PRODUKCJI
    val yearOfManufacture: String? = null,
//    DOPUSZCZALNA ŁADOWNOŚĆ
    val maxPermissibleLoad: String? = null,
//    NAJWIĘKSZY DOP. NACISK OSI
    val maxAxlePressure: String? = null,
//    NR KARTY POJAZDU
    val vehicleCardId: String? = null,
) : Parcelable {

    /**
     * String resource naming the fuel code, or null when the code is not one we know.
     *
     * The model deliberately returns a resource id rather than resolved text: resolving strings is
     * the UI layer's job, and doing it here used to require a globally held Context.
     */
    @get:StringRes
    val fuelTypeLabelRes: Int?
        get() = when (fuelType?.trim()) {
            "P" -> R.string.fuel_type_petrol
            "D" -> R.string.fuel_type_diesel
            "M" -> R.string.fuel_type_mix
            "LPG" -> R.string.fuel_type_liquefied_petroleum_gas
            "CNG" -> R.string.fuel_type_compressed_natural_gas
            "H" -> R.string.fuel_type_hydrogen
            "LNG" -> R.string.fuel_type_liquefied_natural_gas
            "BD" -> R.string.fuel_type_biodiesel
            "E85" -> R.string.fuel_type_ethanol
            "EE" -> R.string.fuel_type_electric_energy
            "999" -> R.string.fuel_type_other
            else -> null
        }
}