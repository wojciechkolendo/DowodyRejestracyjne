package wkolendo.dowodyrejestracyjne.ui.details

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel

internal const val EXTRA_CERTIFICATE = "extra_certificate"

class DetailsViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), DetailsView {

    override val certificate: LiveData<Certificate?> = state.getLiveData(EXTRA_CERTIFICATE)

    override fun onSaveState() = Unit

    fun buildCertificateString(): String? = certificate.value?.let {
        buildString {
            appendValue(it.issuingAuthority, R.string.certificate_issuing_authority)
            appendValue(it.vehicleRegistrationNumber, R.string.certificate_vehicle_registration_number)
            appendValue(it.vehicleManufacturer, R.string.certificate_vehicle_manufacturer)
            appendValue(it.vehicleType, R.string.certificate_vehicle_type)
            appendValue(it.vehicleTypeVariant, R.string.certificate_vehicle_type_variant)
            appendValue(it.vehicleTypeVersion, R.string.certificate_vehicle_type_version)
            appendValue(it.vehicleModel, R.string.certificate_vehicle_model)
            appendValue(it.vehicleIdentificationNumber, R.string.certificate_vin)
            appendValue(it.dateOfFirstRegistration, R.string.certificate_date_of_first_registration)
            appendValue(it.issuingDate, R.string.certificate_issuing_date)
            appendValue(it.expiryDate, R.string.certificate_expiry_date)

            appendValue(it.certificateKeeperName, R.string.certificate_keeper_name)
            appendValue(it.certificateKeeperId, R.string.certificate_keeper_id)
            appendValue(it.certificateKeeperAddress, R.string.certificate_keeper_address)
            appendValue(it.vehicleOwnerName, R.string.certificate_owner_name)
            appendValue(it.vehicleOwnerId, R.string.certificate_owner_id)
            appendValue(it.vehicleOwnerAddress, R.string.certificate_owner_address)

            appendValue(it.maxPermissibleWeight, R.string.certificate_max_permissible_weight)
            appendValue(it.maxAuthorisedWeight, R.string.certificate_max_authorised_weight)
            appendValue(it.maxTrainWeight, R.string.certificate_max_train_weight)
            appendValue(it.vehicleOwnWeight, R.string.certificate_vehicle_own_weight)
            appendValue(it.maxBrakedTrailerMass, R.string.certificate_max_braked_trailer_weight)
            appendValue(it.maxUnbrakedTrailerMass, R.string.certificate_max_unbraked_trailer_weight)
            appendValue(it.typeApprovalNumber, R.string.certificate_type_approval_number)
            appendValue(it.vehicleCategory, R.string.certificate_vehicle_category)
            appendValue(it.numberOfAxles, R.string.certificate_number_of_axles)
            appendValue(it.cylinderCapacity, R.string.certificate_cylinder_capacity)
            appendValue(it.maxNetPower, R.string.certificate_max_net_power)
            appendValue(it.fuelType, R.string.certificate_fuel_type)
            appendValue(it.powerWeightRatio, R.string.certificate_power_weight_ratio)
            appendValue(it.numberOfSeats, R.string.certificate_number_of_seats)
            appendValue(it.numberOfStandingPlaces, R.string.certificate_number_of_standing_places)

            appendValue(it.vehicleClass, R.string.certificate_vehicle_class)
            appendValue(it.purpose, R.string.certificate_purpose)
            appendValue(it.yearOfManufacture, R.string.certificate_year_of_manufacture)
            appendValue(it.maxPermissibleLoad, R.string.certificate_max_permissible_load)
            appendValue(it.maxAxlePressure, R.string.certificate_max_axle_pressure)
            appendValue(it.vehicleCardId, R.string.certificate_vehicle_card_id)
            appendValue(it.series, R.string.certificate_series)
        }.trim()
    }

    private fun StringBuilder.appendValue(value: String?, @StringRes labelRes: Int) {
        value?.takeIf { it.isNotBlank() }?.also { appendLine("${labelRes.getText()}: $it") }
    }
}