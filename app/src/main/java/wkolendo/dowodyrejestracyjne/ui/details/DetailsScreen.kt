package wkolendo.dowodyrejestracyjne.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme
import wkolendo.dowodyrejestracyjne.utils.Screen
import wkolendo.dowodyrejestracyjne.utils.TrackScreen

/** Read-only view of a scanned certificate, split into the same four cards the XML layout used. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    certificate: Certificate,
    onBack: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onCopyValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    TrackScreen(Screen.DETAILS)

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(certificate.vehicleRegistrationNumber.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_left_24dp),
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCopy) {
                        Icon(
                            painter = painterResource(R.drawable.ic_copy_on_surface_24dp),
                            contentDescription = stringResource(R.string.details_copy),
                        )
                    }
                    IconButton(onClick = onShare) {
                        Icon(
                            painter = painterResource(R.drawable.ic_share_on_surface_24dp),
                            contentDescription = stringResource(R.string.details_share),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        DetailsContent(certificate, onCopyValue, Modifier.padding(contentPadding))
    }
}

@Composable
private fun DetailsContent(
    certificate: Certificate,
    onCopyValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BasicsSection(certificate, onCopyValue, Modifier.padding(top = 12.dp))
        OwnersSection(certificate)
        ParamsSection(certificate)
        MiscSection(certificate, Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun DetailsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}

/** Two fields side by side, as the ConstraintLayout chains did. */
@Composable
private fun FieldRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

@Composable
private fun BasicsSection(
    certificate: Certificate,
    onCopyValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DetailsCard(modifier) {
        DetailField(R.string.certificate_issuing_authority, certificate.issuingAuthority, icon = R.drawable.ic_bank_20dp)
        DetailField(R.string.certificate_vehicle_registration_number, certificate.vehicleRegistrationNumber, icon = R.drawable.ic_car_20dp)
        DetailField(R.string.certificate_vehicle_manufacturer, certificate.vehicleManufacturer)
        DetailField(R.string.certificate_vehicle_type, certificate.vehicleType)
        FieldRow {
            DetailField(R.string.certificate_vehicle_type_variant, certificate.vehicleTypeVariant, Modifier.weight(1f))
            DetailField(R.string.certificate_vehicle_type_version, certificate.vehicleTypeVersion, Modifier.weight(1f))
        }
        DetailField(R.string.certificate_vehicle_model, certificate.vehicleModel)
        DetailField(
            R.string.certificate_vin,
            certificate.vehicleIdentificationNumber,
            onCopy = { certificate.vehicleIdentificationNumber?.let(onCopyValue) },
        )
        DetailField(R.string.certificate_date_of_first_registration, certificate.dateOfFirstRegistration, icon = R.drawable.ic_calendar_day_20dp)
        FieldRow {
            DetailField(R.string.certificate_issuing_date, certificate.issuingDate, Modifier.weight(1f))
            DetailField(R.string.certificate_expiry_date, certificate.expiryDate, Modifier.weight(1f))
        }
    }
}

@Composable
private fun OwnersSection(certificate: Certificate, modifier: Modifier = Modifier) {
    DetailsCard(modifier) {
        DetailField(R.string.certificate_keeper_name, certificate.certificateKeeperName, icon = R.drawable.ic_user_20dp)
        DetailField(R.string.certificate_keeper_id, certificate.certificateKeeperId)
        DetailField(R.string.certificate_keeper_address, certificate.certificateKeeperAddress)
        DetailField(R.string.certificate_owner_name, certificate.vehicleOwnerName, icon = R.drawable.ic_user_20dp)
        DetailField(R.string.certificate_owner_id, certificate.vehicleOwnerId)
        DetailField(R.string.certificate_owner_address, certificate.vehicleOwnerAddress)
    }
}

@Composable
private fun ParamsSection(certificate: Certificate, modifier: Modifier = Modifier) {
    DetailsCard(modifier) {
        FieldRow {
            DetailField(
                R.string.certificate_max_permissible_weight,
                certificate.maxPermissibleWeight.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
                icon = R.drawable.ic_weight_hanging_20dp,
            )
            DetailField(
                R.string.certificate_max_authorised_weight,
                certificate.maxAuthorisedWeight.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
        }
        FieldRow {
            DetailField(
                R.string.certificate_max_train_weight,
                certificate.maxTrainWeight.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
            DetailField(
                R.string.certificate_vehicle_own_weight,
                certificate.vehicleOwnWeight.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
        }
        FieldRow {
            DetailField(
                R.string.certificate_max_braked_trailer_weight,
                certificate.maxBrakedTrailerMass.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
            DetailField(
                R.string.certificate_max_unbraked_trailer_weight,
                certificate.maxUnbrakedTrailerMass.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
        }
        DetailField(R.string.certificate_type_approval_number, certificate.typeApprovalNumber)
        FieldRow {
            DetailField(R.string.certificate_vehicle_category, certificate.vehicleCategory, Modifier.weight(1f))
            DetailField(R.string.certificate_number_of_axles, certificate.numberOfAxles, Modifier.weight(1f))
        }
        DetailField(
            R.string.certificate_cylinder_capacity,
            certificate.cylinderCapacity.withUnit(R.string.format_cubic_meter),
            icon = R.drawable.ic_engine_20dp,
        )
        DetailField(R.string.certificate_max_net_power, certificate.maxNetPower.withUnit(R.string.format_kilowatt))
        DetailField(R.string.certificate_fuel_type, certificate.fuelTypeLabel(), icon = R.drawable.ic_gas_pump_20dp)
        DetailField(
            R.string.certificate_power_weight_ratio,
            certificate.powerWeightRatio.withUnit(R.string.format_kilowatt_by_kilogram),
        )
        FieldRow {
            DetailField(R.string.certificate_number_of_seats, certificate.numberOfSeats, Modifier.weight(1f))
            DetailField(R.string.certificate_number_of_standing_places, certificate.numberOfStandingPlaces, Modifier.weight(1f))
        }
    }
}

@Composable
private fun MiscSection(certificate: Certificate, modifier: Modifier = Modifier) {
    DetailsCard(modifier) {
        DetailField(R.string.certificate_vehicle_class, certificate.vehicleClass, icon = R.drawable.ic_circle_info_20dp)
        DetailField(R.string.certificate_purpose, certificate.purpose)
        DetailField(R.string.certificate_year_of_manufacture, certificate.yearOfManufacture)
        FieldRow {
            DetailField(
                R.string.certificate_max_permissible_load,
                certificate.maxPermissibleLoad.withUnit(R.string.format_kilogram),
                Modifier.weight(1f),
            )
            DetailField(
                R.string.certificate_max_axle_pressure,
                certificate.maxAxlePressure.withUnit(R.string.format_kilonewton),
                Modifier.weight(1f),
            )
        }
        FieldRow {
            DetailField(R.string.certificate_vehicle_card_id, certificate.vehicleCardId, Modifier.weight(1f))
            DetailField(R.string.certificate_series, certificate.series, Modifier.weight(1f))
        }
    }
}

/** Resolves the fuel code to its label, falling back to the raw code for unknown values. */
@Composable
private fun Certificate.fuelTypeLabel(): String? =
    fuelTypeLabelRes?.let { stringResource(it) } ?: fuelType

private val sampleCertificate = Certificate(
    databaseId = 1,
    series = "BAQ1234567",
    issuingAuthority = "PREZYDENT WROCŁAWIA\nUL. G. ZAPOLSKIEJ 4\n50-032 WROCŁAW",
    vehicleRegistrationNumber = "WY 65461",
    dateOfFirstRegistration = "2009-06-04",
    certificateKeeperName = "BANKOWY FUNDUSZ LEASINGOWY S.A.O/W-WA",
    certificateKeeperId = "47219176700049",
    certificateKeeperAddress = "01-192 WARSZAWA\nLESZNO 14",
    vehicleOwnerName = "BANKOWY FUNDUSZ LEASINGOWY S.A.O/W-WA",
    vehicleOwnerId = "47219176700049",
    vehicleOwnerAddress = "01-192 WARSZAWA\nLESZNO 14",
    vehicleManufacturer = "BMW",
    vehicleType = "392C",
    vehicleTypeVariant = "WA71",
    vehicleTypeVersion = "5A",
    vehicleModel = "320I",
    vehicleIdentificationNumber = "VF32S8HZF44766503",
    maxPermissibleWeight = "1960",
    maxAuthorisedWeight = "1960",
    vehicleOwnWeight = "1480",
    issuingDate = "2019-09-06",
    vehicleCategory = "M1",
    numberOfAxles = "2",
    cylinderCapacity = "1995",
    maxNetPower = "115",
    fuelType = "D",
    numberOfSeats = "5",
    vehicleClass = "SAMOCHÓD OSOBOWY",
    yearOfManufacture = "2009",
    maxAxlePressure = "9,95",
    vehicleCardId = "AAC1234567",
)

@Preview(name = "Szczegóły", showBackground = true, heightDp = 1400)
@Composable
private fun DetailsScreenPreview() {
    DRTheme(dynamicColor = false) {
        DetailsScreen(sampleCertificate, onBack = {}, onCopy = {}, onShare = {}, onCopyValue = {})
    }
}

@Preview(name = "Szczegóły — ciemny", showBackground = true, heightDp = 1400)
@Composable
private fun DetailsScreenDarkPreview() {
    DRTheme(darkTheme = true, dynamicColor = false) {
        DetailsScreen(sampleCertificate, onBack = {}, onCopy = {}, onShare = {}, onCopyValue = {})
    }
}
