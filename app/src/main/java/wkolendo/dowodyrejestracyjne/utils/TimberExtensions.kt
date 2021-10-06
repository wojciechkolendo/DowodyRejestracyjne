@file:JvmName("TimberExtension")

package wkolendo.dowodyrejestracyjne.utils

import timber.log.Timber

internal fun logVerbose(vararg message: Any?) = logVerbose(message.format(), null)

internal fun logVerbose(th: Throwable?) = logVerbose(null, th)

internal fun logVerbose(message: Any?, th: Throwable?) = Timber.v(message = message.toString(), t = th)

internal fun logDebug(vararg message: Any?) = logDebug(message.format(), null)

internal fun logDebug(th: Throwable?) = logDebug(null, th)

internal fun logDebug(message: Any?, th: Throwable?) = Timber.d(message = message.toString(), t = th)

internal fun logInfo(vararg message: Any?) = logInfo(message.format(), null)

internal fun logInfo(th: Throwable?) = logInfo(null, th)

internal fun logInfo(message: Any?, th: Throwable?) = Timber.i(message = message.toString(), t = th)

internal fun logWarning(vararg message: Any?) = logWarning(message.format(), null)

internal fun logWarning(th: Throwable?) = logWarning(null, th)

internal fun logWarning(message: Any?, th: Throwable?) = Timber.w(message = message.toString(), t = th)

internal fun logError(vararg message: Any?) = logError(message.format(), null)

internal fun logError(th: Throwable?) = logError(null, th)

internal fun logError(message: Any?, th: Throwable?) = Timber.e(message = message.toString(), t = th)

private fun Array<*>.format() = joinToString(separator = " ")
