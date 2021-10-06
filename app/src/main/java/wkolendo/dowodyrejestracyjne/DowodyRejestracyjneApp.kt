package wkolendo.dowodyrejestracyjne

import android.app.Application
import android.content.Context
import android.util.Log
import timber.log.Timber

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
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}

val appContext: Context get() = DowodyRejestracyjneApp.context