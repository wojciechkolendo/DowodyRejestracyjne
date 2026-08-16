package wkolendo.dowodyrejestracyjne.models

/**
 * Builds a [Certificate] out of the decoded Aztec payload.
 *
 * The payload is one long pipe-delimited string whose field order is fixed by the certificate
 * format, so the indices below are the specification, not magic numbers. Some entries span several
 * fields (an address is split across six), which [joinNotEmpty] stitches back together.
 *
 * Kept free of Android dependencies so it can be unit tested on the JVM.
 *
 * @throws IndexOutOfBoundsException when the payload has fewer fields than the format requires.
 */
internal fun String.toCertificate(): Certificate {
    val data = split('|')
    return Certificate(
        series = data[1],
        issuingAuthority = data.joinNotEmpty(range = 3..6),
        vehicleRegistrationNumber = data[7],
        vehicleManufacturer = data[8],
        vehicleType = data[9],
        vehicleTypeVariant = data[10],
        vehicleTypeVersion = data[11],
        vehicleModel = data[12],
        vehicleIdentificationNumber = data[13],
        issuingDate = data[14],
        expiryDate = data[15],

        certificateKeeperName = data.joinNotEmpty(range = 16..19),
        certificateKeeperId = data[20],
        certificateKeeperAddress = data.joinNotEmpty(range = 21..26, appendLine = false),

        vehicleOwnerName = data.joinNotEmpty(range = 27..30),
        vehicleOwnerId = data[31],
        vehicleOwnerAddress = data.joinNotEmpty(range = 32..37, appendLine = false),

        maxPermissibleWeight = data[38],
        maxAuthorisedWeight = data[39],
        maxTrainWeight = data[40],
        vehicleOwnWeight = data[41],
        vehicleCategory = data[42],
        typeApprovalNumber = data[43],
        numberOfAxles = data[44],
        maxBrakedTrailerMass = data[45],
        maxUnbrakedTrailerMass = data[46],
        powerWeightRatio = data[47],
        cylinderCapacity = data[48],
        maxNetPower = data[49],
        fuelType = data[50],
        dateOfFirstRegistration = data[51],
        numberOfSeats = data[52],
        numberOfStandingPlaces = data[53],

        vehicleClass = data[54],
        purpose = data[55],
        yearOfManufacture = data[56],
        maxPermissibleLoad = data[57],
        maxAxlePressure = data[58],
        vehicleCardId = data[59],
    )
}

private fun List<String>.joinNotEmpty(range: IntRange, appendLine: Boolean = true) = buildString {
    for (i in range) {
        this@joinNotEmpty[i].takeIf { it.isNotBlank() }?.also {
            if (appendLine && i != range.first) appendLine()
            append(it)
        }
    }
}
