package wkolendo.dowodyrejestracyjne.viewmodels

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wkolendo.dowodyrejestracyjne.DowodyRejestracyjneApplication
import wkolendo.dowodyrejestracyjne.R
import java.lang.StringBuilder

/**
 * @author Wojtek Kolendo
 */
class ResultViewModel : ViewModel() {

	val SEPARATOR = '|'

	var result: String? = null
	var prettyResult: String? = null
	val decodedFinishedState = MutableLiveData<Boolean>()

	var organWydajacy: String? = null
	var numerRejestracyjny: String? = null
	var danePojazdu: String? = null
	var vin: String? = null
	var dataB: String? = null
	var dataI: String? = null
	var dataH: String? = null

	var posiadaczDowodu: String? = null
	var wlascicielPojazdu: String? = null
	var masaF1: String? = null
	var masaF2: String? = null
	var masaF3: String? = null
	var masaWlasnaPojazdu: String? = null
	var kategoriaPojazdu: String? = null
	var nrHomologacji: String? = null
	var liczbaOsi: String? = null
	var masaO1: String? = null
	var masaO2: String? = null
	var pojemnoscSilnika: String? = null
	var mocSilnika: String? = null
	var rodzajPaliwa: String? = null
	var stosunekMocy: String? = null
	var miejscaSiedzace: String? = null
	var miejscaStojace: String? = null

	var rodzajPojazdu: String? = null
	var przeznaczenie: String? = null
	var rokProdukcji: String? = null
	var dopuszczalnaLadownosc: String? = null
	var naciskOsi: String? = null
	var nrKartyPojazdu: String? = null


	fun decodeResult() {
		if (result != null) {
			var separatorIndex = 0
			for (i in 0 until result!!.length) {
				if (result!![i] == SEPARATOR) {
					separatorIndex++

					// separate result and assign to values
					when (separatorIndex) {
						in 3..6 -> {
							if (organWydajacy == null) {
								organWydajacy = substringSection(i)
							} else {
								substringSection(i)?.let { organWydajacy += "\n" + it }
							}
						}
						7 -> numerRejestracyjny = substringSection(i)
						in 8..12 -> {
							if (danePojazdu == null) {
								danePojazdu = substringSection(i)
							} else {
								substringSection(i)?.let { danePojazdu += "\n" + it }
							}
						}
						13 -> vin = substringSection(i)
						14 -> dataI = substringSection(i)
						15 -> dataH = substringSection(i)
						in 16..26 -> {
							if (posiadaczDowodu == null) {
								posiadaczDowodu = substringSection(i)
							} else {
								substringSection(i)?.let { posiadaczDowodu += "\n" + it }
							}
						}
						in 27..37 -> {
							if (wlascicielPojazdu == null) {
								wlascicielPojazdu = substringSection(i)
							} else {
								substringSection(i)?.let { wlascicielPojazdu += "\n" + it }
							}
						}
						38 -> masaF1 = substringSection(i)
						39 -> masaF2 = substringSection(i)
						40 -> masaF3 = substringSection(i)
						41 -> masaWlasnaPojazdu = substringSection(i)
						42 -> kategoriaPojazdu = substringSection(i)
						43 -> nrHomologacji = substringSection(i)
						44 -> liczbaOsi = substringSection(i)
						45 -> masaO1 = substringSection(i)
						46 -> masaO2 = substringSection(i)
						47 -> stosunekMocy = substringSection(i)
						48 -> pojemnoscSilnika = substringSection(i)
						49 -> mocSilnika = substringSection(i)
						50 -> rodzajPaliwa = substringSection(i)
						51 -> dataB = substringSection(i)
						52 -> miejscaSiedzace = substringSection(i)
						53 -> miejscaStojace = substringSection(i)
						54 -> rodzajPojazdu = substringSection(i)
						55 -> przeznaczenie = substringSection(i)
						56 -> rokProdukcji = substringSection(i)
						57 -> dopuszczalnaLadownosc = substringSection(i)
						58 -> naciskOsi = substringSection(i)
						59 -> nrKartyPojazdu = substringSection(i)
					}
				}
			}

			decodedFinishedState.value = true
		}
	}

	private fun substringSection(startIndex: Int): String? {
		val result = result?.substring(startIndex + 1)?.substringBefore(SEPARATOR, "")
		return if (TextUtils.isEmpty(result)) {
			null
		} else {
			result
		}
	}

	fun getPrettyText(): String? {
		if (prettyResult == null) {
			val builder = StringBuilder()

			organWydajacy?.let { builder.appendln(getString(R.string.result_page1_organ_wydajacy)).appendln(it).appendln() }
			numerRejestracyjny?.let { builder.appendln(getString(R.string.result_page1_a)).appendln(it).appendln() }
			danePojazdu?.let { builder.appendln(getString(R.string.result_page1_d)).appendln(it).appendln() }
			vin?.let { builder.appendln(getString(R.string.result_page1_e)).appendln(it).appendln() }
			dataB?.let { builder.appendln(getString(R.string.result_page1_b)).appendln(it).appendln() }
			dataI?.let { builder.appendln(getString(R.string.result_page1_i)).appendln(it).appendln() }
			dataH?.let { builder.appendln(getString(R.string.result_page1_h)).appendln(it).appendln() }

			posiadaczDowodu?.let { builder.appendln(getString(R.string.result_page2_posiadacz_dowodu)).appendln(it).appendln() }
			wlascicielPojazdu?.let { builder.appendln(getString(R.string.result_page2_wlasciciel_pojazdu)).appendln(it).appendln() }
			masaF1?.let { builder.appendln(getString(R.string.result_page2_masa_f1)).appendln(it).appendln() }
			masaF2?.let { builder.appendln(getString(R.string.result_page2_masa_f2)).appendln(it).appendln() }
			masaF3?.let { builder.appendln(getString(R.string.result_page2_masa_f3)).appendln(it).appendln() }
			masaWlasnaPojazdu?.let { builder.appendln(getString(R.string.result_page2_masa_wlasna_pojazdu)).appendln(it).appendln() }
			kategoriaPojazdu?.let { builder.appendln(getString(R.string.result_page2_kategoria_pojazdu)).appendln(it).appendln() }
			nrHomologacji?.let { builder.appendln(getString(R.string.result_page2_nr_homologacji)).appendln(it).appendln() }
			liczbaOsi?.let { builder.appendln(getString(R.string.result_page2_liczba_osi)).appendln(it).appendln() }
			masaO1?.let { builder.appendln(getString(R.string.result_page2_masa_o1)).appendln(it).appendln() }
			masaO2?.let { builder.appendln(getString(R.string.result_page2_masa_o2)).appendln(it).appendln() }
			pojemnoscSilnika?.let { builder.appendln(getString(R.string.result_page2_pojemnosc_silnika)).appendln(it).appendln() }
			mocSilnika?.let { builder.appendln(getString(R.string.result_page2_moc_silnika)).appendln(it).appendln() }
			rodzajPaliwa?.let { builder.appendln(getString(R.string.result_page2_rodzaj_paliwa)).appendln(it).appendln() }
			stosunekMocy?.let { builder.appendln(getString(R.string.result_page2_stosunek_moc_masa)).appendln(it).appendln() }
			miejscaSiedzace?.let { builder.appendln(getString(R.string.result_page2_m_siedzace)).appendln(it).appendln() }
			miejscaStojace?.let { builder.appendln(getString(R.string.result_page2_m_stojace)).appendln(it).appendln() }

			rodzajPojazdu?.let { builder.appendln(getString(R.string.result_page3_rodzaj_pojazdu)).appendln(it).appendln() }
			przeznaczenie?.let { builder.appendln(getString(R.string.result_page3_przeznaczenie)).appendln(it).appendln() }
			rokProdukcji?.let { builder.appendln(getString(R.string.result_page3_rok_produkcji)).appendln(it).appendln() }
			dopuszczalnaLadownosc?.let { builder.appendln(getString(R.string.result_page3_dopuszczalna_ladownosc)).appendln(it).appendln() }
			naciskOsi?.let { builder.appendln(getString(R.string.result_page3_nacisk_osi)).appendln(it).appendln() }
			nrKartyPojazdu?.let { builder.appendln(getString(R.string.result_page3_nr_karty_pojazdu)).appendln(it) }

			prettyResult = builder.toString()
		}
		return prettyResult
	}

	private fun getString(@StringRes res: Int): String {
		return DowodyRejestracyjneApplication.getContext().getString(res)
	}
}