package wkolendo.dowodyrejestracyjne.models

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Covers the parser that turns a decoded Aztec payload into a [Certificate].
 *
 * The payloads here are synthetic: every field is built by index, which keeps the tests readable and
 * avoids putting a real person's data in the repository.
 */
class CertificateParserTest {

    @Test
    fun `reads single fields by their position`() {
        val certificate = payload(
            7 to "WY 65461",
            8 to "BMW",
            13 to "VF32S8HZF44766503",
            50 to "D",
            59 to "AAC1234567",
        ).toCertificate()

        assertEquals("WY 65461", certificate.vehicleRegistrationNumber)
        assertEquals("BMW", certificate.vehicleManufacturer)
        assertEquals("VF32S8HZF44766503", certificate.vehicleIdentificationNumber)
        assertEquals("D", certificate.fuelType)
        assertEquals("AAC1234567", certificate.vehicleCardId)
    }

    @Test
    fun `joins the issuing authority spread over several fields with line breaks`() {
        val certificate = payload(
            3 to "PREZYDENT WROCŁAWIA",
            4 to "UL. G. ZAPOLSKIEJ 4",
            5 to "50-032 WROCŁAW",
        ).toCertificate()

        assertEquals("PREZYDENT WROCŁAWIA\nUL. G. ZAPOLSKIEJ 4\n50-032 WROCŁAW", certificate.issuingAuthority)
    }

    @Test
    fun `joins an address without line breaks`() {
        val certificate = payload(
            21 to "01-192 WARSZAWA",
            22 to " ",
            23 to "LESZNO 14",
        ).toCertificate()

        assertEquals("01-192 WARSZAWALESZNO 14", certificate.certificateKeeperAddress)
    }

    @Test
    fun `leaves absent fields empty rather than failing`() {
        val certificate = payload(7 to "WY 65461").toCertificate()

        assertEquals("", certificate.vehicleManufacturer)
        assertEquals("", certificate.issuingAuthority)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `rejects a payload with too few fields`() {
        "one|two|three".toCertificate()
    }

    /** Builds a payload of [FIELD_COUNT] pipe-separated fields, with [values] placed by index. */
    private fun payload(vararg values: Pair<Int, String>): String {
        val fields = MutableList(FIELD_COUNT) { "" }
        values.forEach { (index, value) -> fields[index] = value }
        return fields.joinToString("|")
    }

    private companion object {
        /** The format's last index is 59, so a complete payload has 60 fields. */
        const val FIELD_COUNT = 60
    }
}
