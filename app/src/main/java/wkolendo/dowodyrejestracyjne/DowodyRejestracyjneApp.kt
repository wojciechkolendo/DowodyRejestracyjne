package wkolendo.dowodyrejestracyjne

import android.app.Application
import android.content.Context
import timber.log.Timber
import wkolendo.dowodyrejestracyjne.utils.CrashlyticsTree

class DowodyRejestracyjneApp: Application() {

	companion object {
		@JvmStatic
		lateinit var context: Context
			private set
	}

	override fun onCreate() {
		super.onCreate()
		context = this
		initLoggers()
	}

	private fun initLoggers() {
		Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsTree())
	}
}

val appContext: Context get() = DowodyRejestracyjneApp.context