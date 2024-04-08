package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.set
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.utils.error

object UserService {

    private const val COLLECTION_NAME = "USERS"

    suspend fun getUsers(): List<User> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    suspend fun saveOrUpdate(user: User): Boolean {
        return try {
            set(COLLECTION_NAME, user.phoneNumber.toString(), user)
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    suspend fun getUserByPhoneNumber(phoneNumber: String): User? {
        return try {
            getByKey(COLLECTION_NAME, phoneNumber)
        } catch (e: Exception) {
            error(e)
            null
        }
    }

}