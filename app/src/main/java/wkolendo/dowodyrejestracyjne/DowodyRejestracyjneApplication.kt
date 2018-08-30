package wkolendo.dowodyrejestracyjne

import android.app.Application
import android.content.Context
import software.rsquared.androidlogger.Logger

/**
 * @author Wojtek Kolendo
 */
class DowodyRejestracyjneApplication : Application() {

	companion object {
		var instance: DowodyRejestracyjneApplication? = null

		fun getContext(): Context {
			if (instance == null) {
				instance = DowodyRejestracyjneApplication()
			}
			return instance!!.applicationContext
		}
	}

	override fun onCreate() {
		super.onCreate()
		instance = this

		Logger.getLoggerConfig().setLevel(BuildConfig.LOGGER_LEVEL)
	}
}