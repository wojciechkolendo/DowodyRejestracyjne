package wkolendo.dowodyrejestracyjne.utils.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import wkolendo.dowodyrejestracyjne.BR
import wkolendo.dowodyrejestracyjne.utils.setToolbarTitle

private const val EXTRA_TITLE = "extra_title"
private const val NO_TITLE = "NO_TITLE"

abstract class BindingFragment<VDB : ViewDataBinding>(@LayoutRes private val layoutRes: Int? = null) : Fragment() {

    abstract val viewModel: BindingViewModel

    var binding: VDB? = null
        private set

    protected open var toolbarTitle: String = NO_TITLE
        set(value) {
            field = value
            if (value != NO_TITLE) setToolbarTitle(value)
        }

    protected open val toolbarHomeAsUp: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply { toolbarTitle = getString(EXTRA_TITLE, NO_TITLE) }
    }

    override fun onResume() {
        super.onResume()
        if (toolbarTitle != NO_TITLE) setToolbarTitle(toolbarTitle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = layoutRes?.let { DataBindingUtil.inflate(inflater, it, container, false) }
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.setVariable(BR.viewModel, viewModel)
        initToolbar()
        return binding?.root
    }

    protected open fun initToolbar() {
        toolbarHomeAsUp?.also { enabled -> (activity as? BindingActivity<*>)?.supportActionBar?.setDisplayHomeAsUpEnabled(enabled) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TITLE, toolbarTitle)
        viewModel.onSaveState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    open fun openFragment(fragment: Fragment, backStackName: String? = fragment.javaClass.name) =
        (activity as? BindingActivity<*>)?.openFragment(fragment, backStackName = backStackName)

    @MainThread
    protected fun <T> LiveData<T>.observe(observer: (T) -> Unit) { observe(viewLifecycleOwner, Observer(observer)) }
}