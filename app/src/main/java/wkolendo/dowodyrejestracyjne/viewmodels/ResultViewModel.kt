package wkolendo.dowodyrejestracyjne.viewmodels

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Wojtek Kolendo
 */
class ResultViewModel : ViewModel() {

	val SEPARATOR = '|'

	var result: String? = null
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
}