package wkolendo.dowodyrejestracyjne.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Certificate(
//    SERIA
    val series: String?,
//    ORGAN WYDAJĄCY
    val issuingAuthority: String?,
//    A — numer rejestracyjny pojazdu
    val vehicleRegistrationNumber: String?,
//    B — data pierwszej rejestracji pojazdu
    val dateOfFirstRegistration: String?,
//    C.1.1 — nazwisko lub nazwa posiadacza dowodu rejestracyjnego
    val certificateKeeperName: String?,
//    C.1.2 — numer PESEL lub REGON posiadacza dowodu rejestracyjnego
    val certificateKeeperId: String?,
//    C.1.3 — adres posiadacza dowodu rejestracyjnego
    val certificateKeeperAddress: String?,
//    C.2.1 — nazwisko lub nazwa właściciela pojazdu
    val vehicleOwnerName: String?,
//    C.2.2 — numer PESEL lub REGON właściciela pojazdu
    val vehicleOwnerId: String?,
//    C.2.3 — adres właściciela pojazdu
    val vehicleOwnerAddress: String?,
//    D.1 — marka pojazdu
    val vehicleManufacturer: String?,
//    D.2 — typ pojazdu
    val vehicleType: String?,
//    D.2 — wariant, jeżeli występuje
    val vehicleTypeVariant: String?,
//    D.2 — wersja, jeżeli występuje
    val vehicleTypeVersion: String?,
//    D.3 — model pojazdu
    val vehicleModel: String?,
//    E — numer identyfikacyjny pojazdu (numer VIN albo numer nadwozia, podwozia lub ramy)
    val vehicleIdentificationNumber: String?,
//    F.1 — maksymalna masa całkowita pojazdu, wyłączając motocykle i motorowery (w kg)
    val maxPermissibleWeight: String?,
//    F.2 — dopuszczalna masa całkowita pojazdu (w kg)
    val maxAuthorisedWeight: String?,
//    F.3 — dopuszczalna masa całkowita zespołu pojazdów (w kg)
    val maxTrainWeight: String?,
//    G — masa własna pojazdu; w przypadku pojazdu ciągnącego innego niż kategoria M1 masa własna pojazdu obejmuje urządzenie sprzęgające (w kg)
    val vehicleOwnWeight: String?,
//    H — okres ważności dowodu, jeżeli występuje takie ograniczenie
    val expiryDate: String?,
//    I — data wydania dowodu rejestracyjnego
    val issuingDate: String?,
//    J — kategoria pojazdu
    val vehicleCategory: String?,
//    K — numer świadectwa homologacji typu pojazdu, jeżeli występuje
    val typeApprovalNumber: String?,
//    L — liczba osi
    val numberOfAxles: String?,
//    O.1 — maksymalna masa całkowita przyczepy z hamulcem (w kg)
    val maxBrakedTrailerMass: String?,
//    O.2 — maksymalna masa całkowita przyczepy bez hamulca (w kg)
    val maxUnbrakedTrailerMass: String?,
//    P.1 — pojemność silnika (w cm³)
    val cylinderCapacity: String?,
//    P.2 — maksymalna moc netto silnika (w kW)
    val maxNetPower: String?,
//    P.3 — rodzaj paliwa
    val fuelType: String?,
//    Q — stosunek mocy do masy własnej (w kW/kg); dotyczy motocykli i motorowerów
    val powerWeightRatio: String?,
//    S.1 — liczba miejsc siedzących, włączając siedzenie kierowcy
    val numberOfSeats: String?,
//    S.2 — liczba miejsc stojących, jeżeli występuje
    val numberOfStandingPlaces: String?,
//    RODZAJ POJAZDU
    val vehicleClass: String?,
//    PRZEZNACZENIE
    val purpose: String?,
//    ROK PRODUKCJI
    val yearOfManufacture: String?,
//    DOPUSZCZALNA ŁADOWNOŚĆ
    val maxPermissibleLoad: String?,
//    NAJWIĘKSZY DOP. NACISK OSI
    val maxAxlePressure: String?,
//    NR KARTY POJAZDU
    val vehicleCardId: String?,
) : Parcelable