@file:JvmName("TimberExtension")

package wkolendo.dowodyrejestracyjne.utils

import timber.log.Timber

/**
 * Reports a handled failure.
 *
 * In debug this prints through Timber's DebugTree; in release [CrashlyticsTree] turns it into a
 * Crashlytics non-fatal. Never pass anything read off a certificate — see [CrashlyticsTree].
 */
internal fun logError(th: Throwable?) = Timber.e(th)

/** Reports a handled failure with extra context. */
internal fun logError(message: Any?, th: Throwable? = null) =
    if (message == null) Timber.e(th) else Timber.e(th, message.toString())
