package com.gtech.rapidly.utils

import android.util.Log

inline fun <reified T> T.info(message: String) {
    Log.i(T::class.java.simpleName, message)
}

inline fun <reified T> T.debug(message: String) {
    Log.d(T::class.java.simpleName, message)
}

inline fun <reified T> T.error(message: String) {
    Log.e(T::class.java.simpleName, message)
}

inline fun <reified T> T.error(e: Throwable) {
    Log.e(T::class.java.simpleName, e.message ?: "Unknown error", e)
}

inline fun <reified T> T.warning(message: String) {
    Log.w(T::class.java.simpleName, message)
}

inline fun <reified T> T.verbose(message: String) {
    Log.v(T::class.java.simpleName, message)
}