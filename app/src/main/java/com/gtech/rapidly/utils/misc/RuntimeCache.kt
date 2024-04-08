package com.gtech.rapidly.utils.misc

import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.firestore.model.Settings
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.RestaurantService
import com.gtech.rapidly.features.common.firestore.service.SettingsService
import com.gtech.rapidly.utils.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RuntimeCache {

    private lateinit var currentUser: User
    private lateinit var restaurants: List<Restaurant>
    private lateinit var settings: Settings

    fun saveCurrentUser(user: User) {
        currentUser = user
    }

    fun getCurrentUser(): User {
        return currentUser
    }

    fun getRestaurants(): List<Restaurant> {
        return restaurants
    }

    fun getSettings(): Settings {
        return settings
    }

    suspend fun initResources(): Boolean = withContext(Dispatchers.IO) {

        try {

            val restaurants = RestaurantService.getRestaurants()
            if (restaurants.isEmpty()) {
                error("No restaurants found")
                return@withContext false
            }

            val settings = SettingsService.getSetting()
            if (settings == null) {
                error("No settings found")
                return@withContext false
            }

            RuntimeCache.restaurants = restaurants
            RuntimeCache.settings = settings

            true

        } catch (e: Exception) {
            error(e)
            return@withContext false
        }

    }

}