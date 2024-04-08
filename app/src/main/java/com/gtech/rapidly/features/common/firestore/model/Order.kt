package com.gtech.rapidly.features.common.firestore.model

import com.google.firebase.Timestamp
import com.gtech.rapidly.utils.convert.asciiToHex
import com.gtech.rapidly.utils.misc.GTime
import java.io.Serializable

data class Order(
    val orderId: String = "", // USER PHONE NUMBER + BILL NUMBER + TIMESTAMP
    val billNo: String = "",
    val billNoHash: String = "",
    val billAmount: Double = 0.0,
    val restaurantId: Long = 0,
    val deliveryNote: String = "",
    val pickupNote: String = "",
    val deliveryBoyNumber: Long = 0,
    val pickupTime: Long = 0,
    var deliveryTime: Long = 0,
    val pickupLocation: Long = 0,
    val deliveryLocation: Long = 0,
    val customerNumber: Long = 0,
    var orderStatus: Status = Status.PICKUP,
    var scannedText: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp? = null
) : Serializable {
    enum class Status { PICKUP, DELIVERED }
    companion object {

        fun genOrderId(
            deliveryBoyNumber: Long,
            billNumber: String
        ): String {
            return "${asciiToHex(billNumber)}$deliveryBoyNumber${System.currentTimeMillis()}"
        }

        fun genBillNoHash(
            billNumber: String,
            amount: Double
        ): String {
            return asciiToHex(
                "$billNumber$amount${
                    GTime.toTime(
                        System.currentTimeMillis(),
                        "yyyyMMdd"
                    )
                }"
            )
        }

        fun delayTime(pickupTime: Long): Int {
            return if (GTime.getDiffInMinute(pickupTime) < 30) 0
            else GTime.getDiffInMinute(pickupTime)
        }

    }
}
