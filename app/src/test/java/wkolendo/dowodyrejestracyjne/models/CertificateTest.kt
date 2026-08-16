package wkolendo.dowodyrejestracyjne.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import wkolendo.dowodyrejestracyjne.R

/** Covers the fuel code mapping on [Certificate]. */
class CertificateTest {

    @Test
    fun `maps a known fuel code to its label`() {
        assertEquals(R.string.fuel_type_diesel, Certificate(fuelType = "D").fuelTypeLabelRes)
        assertEquals(R.string.fuel_type_petrol, Certificate(fuelType = "P").fuelTypeLabelRes)
        assertEquals(R.string.fuel_type_electric_energy, Certificate(fuelType = "EE").fuelTypeLabelRes)
    }

    @Test
    fun `ignores surrounding whitespace in the code`() {
        assertEquals(R.string.fuel_type_diesel, Certificate(fuelType = "  D ").fuelTypeLabelRes)
    }

    @Test
    fun `returns null for an unknown or missing code, so the UI can fall back to the raw value`() {
        assertNull(Certificate(fuelType = "XYZ").fuelTypeLabelRes)
        assertNull(Certificate(fuelType = "").fuelTypeLabelRes)
        assertNull(Certificate(fuelType = null).fuelTypeLabelRes)
    }
}
