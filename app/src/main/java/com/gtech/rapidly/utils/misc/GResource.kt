package com.gtech.rapidly.utils.misc

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.gtech.rapidly.app.RapidlyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GResource {

    private var resource: SharedPreferences = RapidlyApp
        .instance
        .getSharedPreferences(
            "RAPIDLY_APP_RESOURCE",
            MODE_PRIVATE
        )

    enum class Key {
        IS_LOGIN,
        USER_SESSION,
    }

    suspend fun saveData(key: Key, data: Any) = withContext(Dispatchers.IO) {
        when (data) {
            is Int -> resource.edit().putInt(key.toString(), data).apply()
            is String -> resource.edit().putString(key.toString(), data).apply()
            is Boolean -> resource.edit().putBoolean(key.toString(), data).apply()
            is Float -> resource.edit().putFloat(key.toString(), data).apply()
            is Long -> resource.edit().putLong(key.toString(), data).apply()
        }
    }

    fun getStringData(resourceKey: Key): String {
        return resource.getString(resourceKey.toString(), "") ?: ""
    }

    fun getIntData(resourceKey: Key): Int {
        return resource.getInt(resourceKey.toString(), 0)
    }

    fun getBoolData(resourceKey: Key): Boolean {
        return resource.getBoolean(resourceKey.toString(), false)
    }

    fun getFloatData(resourceKey: String): Float {
        return resource.getFloat(resourceKey, 0F)
    }

    fun getLongData(resourceKey: Key): Long {
        return resource.getLong(resourceKey.toString(), 0L)
    }

}



