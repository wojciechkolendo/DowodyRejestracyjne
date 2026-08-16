package wkolendo.dowodyrejestracyjne.ui.details

import android.content.res.Resources
import androidx.annotation.StringRes
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate

/**
 * Flattens a certificate into the plain text used by the copy and share actions.
 *
 * Takes [Resources] explicitly instead of reaching for a globally held Context, which is what the
 * previous version in `DetailsViewModel` did. Empty fields are skipped.
 */
fun Certificate.toShareableText(resources: Resources): String = buildString {
    appendValue(resources, issuingAuthority, R.string.certificate_issuing_authority)
    appendValue(resources, vehicleRegistrationNumber, R.string.certificate_vehicle_registration_number)
    appendValue(resources, vehicleManufacturer, R.string.certificate_vehicle_manufacturer)
    appendValue(resources, vehicleType, R.string.certificate_vehicle_type)
    appendValue(resources, vehicleTypeVariant, R.string.certificate_vehicle_type_variant)
    appendValue(resources, vehicleTypeVersion, R.string.certificate_vehicle_type_version)
    appendValue(resources, vehicleModel, R.string.certificate_vehicle_model)
    appendValue(resources, vehicleIdentificationNumber, R.string.certificate_vin)
    appendValue(resources, dateOfFirstRegistration, R.string.certificate_date_of_first_registration)
    appendValue(resources, issuingDate, R.string.certificate_issuing_date)
    appendValue(resources, expiryDate, R.string.certificate_expiry_date)

    appendValue(resources, certificateKeeperName, R.string.certificate_keeper_name)
    appendValue(resources, certificateKeeperId, R.string.certificate_keeper_id)
    appendValue(resources, certificateKeeperAddress, R.string.certificate_keeper_address)
    appendValue(resources, vehicleOwnerName, R.string.certificate_owner_name)
    appendValue(resources, vehicleOwnerId, R.string.certificate_owner_id)
    appendValue(resources, vehicleOwnerAddress, R.string.certificate_owner_address)

    appendValue(resources, maxPermissibleWeight, R.string.certificate_max_permissible_weight)
    appendValue(resources, maxAuthorisedWeight, R.string.certificate_max_authorised_weight)
    appendValue(resources, maxTrainWeight, R.string.certificate_max_train_weight)
    appendValue(resources, vehicleOwnWeight, R.string.certificate_vehicle_own_weight)
    appendValue(resources, maxBrakedTrailerMass, R.string.certificate_max_braked_trailer_weight)
    appendValue(resources, maxUnbrakedTrailerMass, R.string.certificate_max_unbraked_trailer_weight)
    appendValue(resources, typeApprovalNumber, R.string.certificate_type_approval_number)
    appendValue(resources, vehicleCategory, R.string.certificate_vehicle_category)
    appendValue(resources, numberOfAxles, R.string.certificate_number_of_axles)
    appendValue(resources, cylinderCapacity, R.string.certificate_cylinder_capacity)
    appendValue(resources, maxNetPower, R.string.certificate_max_net_power)
    appendValue(resources, fuelTypeLabelRes?.let { resources.getString(it) } ?: fuelType, R.string.certificate_fuel_type)
    appendValue(resources, powerWeightRatio, R.string.certificate_power_weight_ratio)
    appendValue(resources, numberOfSeats, R.string.certificate_number_of_seats)
    appendValue(resources, numberOfStandingPlaces, R.string.certificate_number_of_standing_places)

    appendValue(resources, vehicleClass, R.string.certificate_vehicle_class)
    appendValue(resources, purpose, R.string.certificate_purpose)
    appendValue(resources, yearOfManufacture, R.string.certificate_year_of_manufacture)
    appendValue(resources, maxPermissibleLoad, R.string.certificate_max_permissible_load)
    appendValue(resources, maxAxlePressure, R.string.certificate_max_axle_pressure)
    appendValue(resources, vehicleCardId, R.string.certificate_vehicle_card_id)
    appendValue(resources, series, R.string.certificate_series)
}.trim()

private fun StringBuilder.appendValue(resources: Resources, value: String?, @StringRes labelRes: Int) {
    value?.takeIf { it.isNotBlank() }?.also { appendLine("${resources.getString(labelRes)}: $it") }
}
