package com.gtech.rapidly.features.common.firestore.model

import java.io.Serializable

data class User(
    val name: String = "",
    val phoneNumber: Long = 0,
    val addharCardNumber: String = "",
    val drivingLicenceNumber: String = "",
    val vehicleNumber: String = "",
    val password: String = "",
    var totalSalary: Double = 0.0,
    var totalWithdrawal: Double = 0.0,
    var totalPenalties: Double = 0.0,
    var orderCount: Int = 0,
    var assignedRestaurantId: Long = 0,
    var permissions: List<Permission> = emptyList(),
    var upiId: String = "",
    var userType: UserType = UserType.DELIVERY_BOY,
    var status: Status = Status.INACTIVE
): Serializable {

    enum class UserType {
        DELIVERY_BOY,
        ADMIN
    }

    enum class Status {
        ACTIVE,
        INACTIVE
    }

    enum class Permission {
        VIEW_COMPANY_STATISTICS,
        VIEW_DELIVERY_BOY_STATISTICS
    }

    fun hasPermission(permission: Permission): Boolean {
        return permissions.contains(permission)
    }

}
