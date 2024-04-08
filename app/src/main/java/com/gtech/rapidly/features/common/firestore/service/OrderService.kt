package com.gtech.rapidly.features.common.firestore.service

import com.google.firebase.firestore.FirebaseFirestore
import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.set
import com.gtech.rapidly.utils.error
import kotlinx.coroutines.tasks.await

object OrderService {

    private const val COLLECTION_NAME = "ORDERS"

    suspend fun getOrders(): List<Order> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

    suspend fun saveOrUpdate(order: Order): Boolean {
        return try {
            set(COLLECTION_NAME, order.orderId, order)
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    suspend fun getByOrderId(orderId: String): Order? {
        return try {
            getByKey(COLLECTION_NAME, orderId)
        } catch (e: Exception) {
            error(e)
            null
        }
    }

    suspend fun getByBillNoHash(billNoHash: String): Order? {
        return try {
            val db = FirebaseFirestore.getInstance()
            val querySnapshot = db
                .collection(COLLECTION_NAME)
                .whereEqualTo("billNoHash", billNoHash)
                .get()
                .await()
            querySnapshot?.documents?.firstOrNull()?.toObject(Order::class.java)
        } catch (e: Exception) {
            error(e)
            null
        }
    }

    suspend fun getByStatus(status: Order.Status, deliverBoyNumber: Long, limit: Long = 100): MutableList<Order> {

        // GET DB INSTANCE
        val db = FirebaseFirestore.getInstance()
        val dataList = mutableListOf<Order>()

        // EXECUTE QUERY
        val querySnapshot = db
            .collection(COLLECTION_NAME)
            .whereEqualTo("orderStatus", status)
            .whereEqualTo("deliveryBoyNumber", deliverBoyNumber)
            .limit(limit)
            .get()
            .await()

        querySnapshot?.documents?.forEach {
            val dataObj = try {
                it.toObject(Order::class.java)
            } catch (e: Exception) {
                error(e)
                null
            }
            if (dataObj != null) {
                dataList.add(dataObj)
            }
        }

        return dataList

    }

}