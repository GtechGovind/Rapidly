package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.model.Settings
import com.gtech.rapidly.features.common.firestore.set
import com.gtech.rapidly.utils.error

object SettingsService {

    private const val COLLECTION_NAME = "SETTINGS"
    private const val SETTINGS_VERSION = "V1"

    suspend fun getSetting(id: String = SETTINGS_VERSION): Settings? {
        return try {
            getByKey(COLLECTION_NAME, id)
        } catch (e: Exception) {
            error(e)
            null
        }
    }

    suspend fun getSettings(): List<Settings> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    suspend fun setSettings(settings: Settings): Boolean {
        return try {
            set(COLLECTION_NAME, settings)
            true
        } catch (e: Exception) {
            error(e)
            return false
        }
    }

}