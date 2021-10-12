package wkolendo.dowodyrejestracyjne.ui.start

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.FragmentStartBinding
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.details.DetailsFragment
import wkolendo.dowodyrejestracyjne.ui.settings.SettingsFragment
import wkolendo.dowodyrejestracyjne.ui.start.scan.CameraScannerDialogFragment
import wkolendo.dowodyrejestracyjne.utils.logError
import wkolendo.dowodyrejestracyjne.utils.showDialogMessage
import wkolendo.dowodyrejestracyjne.utils.ui.BindingFragment
import wkolendo.dowodyrejestracyjne.utils.vibrateTap

class StartFragment : BindingFragment<FragmentStartBinding>(R.layout.fragment_start) {

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), ::cameraPermissionCallback)

    override val toolbarHomeAsUp = false

    override val viewModel: StartViewModel by viewModels()

    override var toolbarTitle = "Czytnik DR"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.settings?.setOnClickListener { openSettingsFragment() }
        binding?.addNew?.setOnClickListener { startNewScan() }
        lifecycleScope.launch { collectFlows() }
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                viewModel.newScanEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
//                    logError("collected $it")
//                }
//            }
//        }
    }

    private suspend fun collectFlows() {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.openCertificateFlow.collect { openDetailsFragment(it) }
        }
    }

    private fun openDetailsFragment(certificate: Certificate) = openFragment(DetailsFragment.newInstance(certificate))

    private fun openSettingsFragment() = openFragment(SettingsFragment())

    private fun cameraPermissionCallback(isGranted: Boolean) {
        if (isGranted) openCameraDialog()
        else showDialogMessage("Uprawnienia kamery są niezbędne do zeskanowania kodu.")

    }

    private fun startNewScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) openCameraDialog()
        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun openCameraDialog() {
        CameraScannerDialogFragment().apply {
            isCancelable = true
            show(this@StartFragment.childFragmentManager, "scanner_dialog")
        }
    }
}