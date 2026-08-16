package wkolendo.dowodyrejestracyjne.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Forwards Timber output to Crashlytics in release builds, where no other tree is planted and the
 * logs would otherwise go nowhere.
 *
 * Only warnings and above are kept: a breadcrumb trail for whatever crash or handled exception
 * follows. Throwables are additionally reported as non-fatals, which is how scan failures and
 * camera binding errors become visible without a crash.
 *
 * Nothing here may carry certificate data — every call site passes a Throwable or a short message,
 * and it has to stay that way. The app handles names, addresses and PESEL numbers.
 */
class CrashlyticsTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.WARN

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(if (tag != null) "$tag: $message" else message)
        if (t != null && priority >= Log.ERROR) crashlytics.recordException(t)
    }
}
