package wkolendo.dowodyrejestracyjne.views.activities

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.views.dialogs.LoadingDialog

/**
 * @author Wojtek Kolendo
 */
abstract class DowodyRejestracyjneActivity : AppCompatActivity() {

	var loadingDialog: LoadingDialog? = null

	private fun showSnackMessage(snackbar: Snackbar): Snackbar {
		snackbar.show()
		return snackbar
	}

	fun showSnackMessage(@StringRes msg: Int): Snackbar {
		return showSnackMessage(msg, Snackbar.LENGTH_LONG)
	}

	fun showSnackMessage(@StringRes msg: Int, duration: Int): Snackbar {
		return showSnackMessage(Snackbar.make(getRootView(), msg, duration))
	}

	fun showSnackMessage(msg: String): Snackbar {
		return showSnackMessage(msg, Snackbar.LENGTH_LONG)
	}

	fun showSnackMessage(msg: String, duration: Int): Snackbar {
		return showSnackMessage(Snackbar.make(getRootView(), msg, duration))
	}

	fun showDialogMessage(msg: String, postAction: Runnable?) {
		AlertDialog.Builder(this, R.style.Theme_DowodyRejestracyjne_Dialog)
				.setCancelable(false)
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok) { _, _ -> postAction?.run() }
				.show()
	}

	fun showDialogMessage(@StringRes msg: Int, postAction: Runnable?) {
		AlertDialog.Builder(this, R.style.Theme_DowodyRejestracyjne_Dialog)
				.setCancelable(false)
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok) { _, _ -> postAction?.run() }
				.show()
	}

	open fun showLoading(show: Boolean) {
		if (show) {
			if (loadingDialog == null) {
				loadingDialog = LoadingDialog(this, R.style.Theme_DowodyRejestracyjne_ProgressDialog)
			}
			if (!loadingDialog!!.isShowing) {
				loadingDialog?.show()
			}
		} else {
			if (loadingDialog != null && loadingDialog!!.isShowing) {
				loadingDialog?.dismiss()
			}
		}
	}

	fun getRootView(): ViewGroup {
		return this.findViewById(android.R.id.content) as ViewGroup
	}
}