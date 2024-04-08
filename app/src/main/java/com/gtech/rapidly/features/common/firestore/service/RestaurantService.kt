package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.utils.error

object RestaurantService {

    private const val COLLECTION_NAME = "RESTAURANTS"

    suspend fun getRestaurants(): List<Restaurant> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    suspend fun getById(id: Long): Restaurant? {
        return try {
            getByKey(COLLECTION_NAME, id.toString())
        } catch (e: Exception) {
            error(e)
            null
        }
    }

}