package wkolendo.dowodyrejestracyjne.utils.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import wkolendo.dowodyrejestracyjne.BR
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.getText
import wkolendo.dowodyrejestracyjne.utils.logDebug

abstract class BindingActivity<VDB : ViewDataBinding>(@LayoutRes private val layoutRes: Int? = null) :
    AppCompatActivity() {

    var binding: VDB? = null
        private set

    protected abstract val viewModel: BindingViewModel

    @IdRes
    protected open val fragmentContainerId = R.id.fragment_container

    protected open val toolbarHomeAsUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = layoutRes?.let { DataBindingUtil.setContentView(this, it) }
        binding?.lifecycleOwner = this
        binding?.setVariable(BR.viewModel, viewModel)
        initToolbar()
        supportFragmentManager.takeIf { savedInstanceState == null }?.initFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    protected open fun initToolbar() {
        binding?.root?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(toolbarHomeAsUp)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveState()
    }

    protected open fun getInitialFragment(): Fragment? = null

    protected open fun FragmentManager.initFragment() {
        getInitialFragment()?.also { commit { add(fragmentContainerId, it) } }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @JvmOverloads
    open fun openFragment(fragment: Fragment, addToBackStack: Boolean = true, backStackName: String? = fragment.javaClass.name) {
        logDebug("Open ${fragment.javaClass.simpleName}${if (addToBackStack) " and add to back stack" else ""} (${supportFragmentManager.backStackEntryCount} on stack)")
        supportFragmentManager.beginTransaction().apply {
            replace(fragmentContainerId, fragment)
            if (addToBackStack) {
                setReorderingAllowed(true)
                addToBackStack(backStackName)
            }
        }.commit()
    }

    fun setToolbarTitle(@StringRes title: Int) = setToolbarTitle(title.getText())

    open fun setToolbarTitle(title: CharSequence?) {
        if (this.title != title || supportActionBar?.title != title) {
            this.title = title
            supportActionBar?.title = title
        }
    }

    open fun getRootView() = binding?.root as ViewGroup

    @MainThread
    protected fun <T> LiveData<T>.observe(observer: (T) -> Unit) = observe(this@BindingActivity, Observer(observer))
}