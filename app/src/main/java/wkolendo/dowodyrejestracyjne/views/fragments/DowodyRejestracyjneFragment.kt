package wkolendo.dowodyrejestracyjne.views.fragments

import android.content.Context
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import wkolendo.dowodyrejestracyjne.views.activities.DowodyRejestracyjneActivity

/**
 * @author Wojtek Kolendo
 */
abstract class DowodyRejestracyjneFragment : Fragment() {

	@CallSuper
	override fun onAttach(context: Context) {
		super.onAttach(context)
		if (context !is DowodyRejestracyjneActivity) {
			throw RuntimeException(context.toString() + " must extends DowodyRejestracyjneActivity")
		}
	}

	protected fun showLoading(show: Boolean) {
		(activity as DowodyRejestracyjneActivity).showLoading(show)
	}

	protected fun showSnackMessage(@StringRes msg: Int): Snackbar {
		return (activity as DowodyRejestracyjneActivity).showSnackMessage(msg)
	}

	protected fun showSnackMessage(@StringRes msg: Int, duration: Int): Snackbar {
		return (activity as DowodyRejestracyjneActivity).showSnackMessage(msg, duration)
	}

	protected fun showSnackMessage(msg: String): Snackbar {
		return (activity as DowodyRejestracyjneActivity).showSnackMessage(msg)
	}

	protected fun showSnackMessage(msg: String, duration: Int): Snackbar {
		return (activity as DowodyRejestracyjneActivity).showSnackMessage(msg, duration)
	}

	protected fun showDialogMessage(msg: String, postAction: Runnable?) {
		(activity as DowodyRejestracyjneActivity).showDialogMessage(msg, postAction)
	}

	protected fun showDialogMessage(@StringRes msg: Int, postAction: Runnable?) {
		(activity as DowodyRejestracyjneActivity).showDialogMessage(msg, postAction)
	}
}