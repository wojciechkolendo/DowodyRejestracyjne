package wkolendo.dowodyrejestracyjne.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.part_result_page1.*
import kotlinx.android.synthetic.main.part_result_page2.*
import kotlinx.android.synthetic.main.part_result_page3.*
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.viewmodels.ResultViewModel

/**
 * @author Wojtek Kolendo
 */
class ResultFragment : DowodyRejestracyjneFragment() {

	private lateinit var viewModel: ResultViewModel

	companion object {
		/**
		 * Plain text received from AZTEC code
		 */
		val EXTRA_RESULT = "extra_result"

		@JvmStatic
		fun newInstance(result: String): ResultFragment {
			val args = Bundle()
			args.putString(EXTRA_RESULT, result)
			return ResultFragment().apply { arguments = args }
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity()).get(ResultViewModel::class.java)
		viewModel.decodedFinishedState.observe(this, Observer<Boolean> { decodedState ->
			if (decodedState) fillDataFields()
		})

		if (savedInstanceState == null) {
			val result = arguments?.getString(EXTRA_RESULT)
			result?.let { viewModel.result = it }
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_result, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.decodeResult()
	}

	private fun fillDataFields() {
		viewModel.organWydajacy?.let { organWydajacyTextView.setText(it) }
		viewModel.numerRejestracyjny?.let { numerRejestracyjnyTextView.setText(it) }
		viewModel.danePojazdu?.let { danePojazduTextView.setText(it) }
		viewModel.vin?.let { vinTextView.setText(it) }
		viewModel.dataB?.let { dataBTextView.setText(it) }
		viewModel.dataI?.let { dataITextView.setText(it) }
		viewModel.dataH?.let { dataHTextView.setText(it) }

		viewModel.posiadaczDowodu?.let { posiadaczDowoduTextView.setText(it) }
		viewModel.wlascicielPojazdu?.let { wlascicielPojazduTextView.setText(it) }
		viewModel.masaF1?.let { masaF1TextView.setText(it) }
		viewModel.masaF2?.let { masaF2TextView.setText(it) }
		viewModel.masaF3?.let { masaF3TextView.setText(it) }
		viewModel.masaWlasnaPojazdu?.let { masaWlasnaTextView.setText(it) }
		viewModel.kategoriaPojazdu?.let { kategoriaPojazduTextView.setText(it) }
		viewModel.nrHomologacji?.let { nrHomologacjiTextView.setText(it) }
		viewModel.liczbaOsi?.let { liczbaOsiTextView.setText(it) }
		viewModel.masaO1?.let { masaO1TextView.setText(it) }
		viewModel.masaO2?.let { masaO2TextView.setText(it) }
		viewModel.pojemnoscSilnika?.let { pojemnoscSilnikaTextView.setText(it) }
		viewModel.mocSilnika?.let { mocSilnikaTextView.setText(it) }
		viewModel.rodzajPaliwa?.let { rodzajPaliwaTextView.setText(it) }
		viewModel.stosunekMocy?.let { stosunekMocMasaTextView.setText(it) }
		viewModel.miejscaSiedzace?.let { miejscaSiedzaceTextView.setText(it) }
		viewModel.miejscaStojace?.let { miejscaStojaceTextView.setText(it) }

		viewModel.rodzajPojazdu?.let { rodzajPojazduTextView.setText(it) }
		viewModel.przeznaczenie?.let { przeznaczenieTextView.setText(it) }
		viewModel.rokProdukcji?.let { rokProdukcjiTextView.setText(it) }
		viewModel.dopuszczalnaLadownosc?.let { dopuszczalnaLadownoscTextView.setText(it) }
		viewModel.naciskOsi?.let { naciskOsiTextView.setText(it) }
		viewModel.nrKartyPojazdu?.let { nrKartyPojazduTextView.setText(it) }

	}

}