package com.gtech.rapidly.features.common.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend inline fun <reified T : Any> getAll(collectionPath: String): List<T> {

    // GET DB INSTANCE
    val db = FirebaseFirestore.getInstance()
    val dataList = mutableListOf<T>()

    // EXECUTE QUERY
    val querySnapshot = db
        .collection(collectionPath)
        .get()
        .await()


    querySnapshot?.documents?.forEach {
        val dataObj = it.toObject(T::class.java)
        if (dataObj != null) {
            dataList.add(dataObj)
        }
    }

    return dataList

}

suspend inline fun <reified T : Any> getByKey(collectionPath: String, key: String): T? {

    // GET DB INSTANCE
    val db = FirebaseFirestore.getInstance()

    // EXECUTE QUERY
    val document = db
        .collection(collectionPath)
        .document(key)
        .get()
        .await()


    if (document.exists()) {
        val dataObj = document.toObject(T::class.java)
        if (dataObj != null) {
            return dataObj
        }
    }

    return null

}

suspend inline fun <reified T : Any> set(collectionPath: String, key: String, data: T) {

    // GET DB INSTANCE
    val db = FirebaseFirestore.getInstance()

    // EXECUTE QUERY
    db
        .collection(collectionPath)
        .document(key)
        .set(data)
        .await()

}

suspend inline fun <reified T : Any> set(collectionPath: String, data: T) {

    // GET DB INSTANCE
    val db = FirebaseFirestore.getInstance()

    // EXECUTE QUERY
    db
        .collection(collectionPath)
        .document()
        .set(data)
        .await()

}
