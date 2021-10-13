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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.databinding.FragmentStartBinding
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.details.DetailsFragment
import wkolendo.dowodyrejestracyjne.ui.settings.SettingsFragment
import wkolendo.dowodyrejestracyjne.ui.start.scan.CameraScannerDialogFragment
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.showDialogMessage
import wkolendo.dowodyrejestracyjne.utils.ui.BindingFragment
import wkolendo.dowodyrejestracyjne.utils.vibrateError

class StartFragment : BindingFragment<FragmentStartBinding>(R.layout.fragment_start) {

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), ::cameraPermissionCallback)

    override val toolbarHomeAsUp = false

    override val viewModel: StartViewModel by viewModels()

    override var toolbarTitle = R.string.app_name.getText()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.settings?.setOnClickListener { openSettingsFragment() }
        binding?.addNew?.setOnClickListener { startNewScan() }
        lifecycleScope.launch { collectFlows() }
    }

    private suspend fun collectFlows() {
        viewModel.eventsFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
            when (it) {
                is StartViewModel.Event.OpenDetails -> openDetailsFragment(it.certificate)
                is StartViewModel.Event.ShowError -> showDialogMessage(it.textRes).also { context?.vibrateError() }
            }
        }
    }

    private fun openDetailsFragment(certificate: Certificate) = openFragment(DetailsFragment.newInstance(certificate))

    private fun openSettingsFragment() = openFragment(SettingsFragment())

    private fun cameraPermissionCallback(isGranted: Boolean) {
        if (isGranted) openCameraDialog()
        else showDialogMessage(R.string.start_camera_permission_message)

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