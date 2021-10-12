package wkolendo.dowodyrejestracyjne.utils.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import wkolendo.dowodyrejestracyjne.BR

abstract class BindingDialogFragment<VDB : ViewDataBinding>(@LayoutRes private val layoutRes: Int? = null) : DialogFragment() {

    private var dialogView: View? = null

    abstract val viewModel: BindingViewModel

    var binding: VDB? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = dialogView.also {
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = createDialogBuilder().apply {
            setView(onCreateDialogView(savedInstanceState).also { dialogView = it })
            onBuildDialog(this)
        }.create()
        dialog.setOnShowListener { onShow(it) }
        return dialog
    }

    protected open fun onCreateDialogView(savedInstanceState: Bundle?): View? {
        binding = layoutRes?.let { DataBindingUtil.inflate(LayoutInflater.from(activity), it, null, false) }
        binding?.setVariable(BR.viewModel, viewModel)
        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveState()
    }

    protected open fun createDialogBuilder() = AlertDialog.Builder(requireActivity())

    protected open fun onBuildDialog(builder: AlertDialog.Builder) {}

    protected open fun onShow(dialog: DialogInterface) {}

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @MainThread
    protected fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer(observer))
    }
}