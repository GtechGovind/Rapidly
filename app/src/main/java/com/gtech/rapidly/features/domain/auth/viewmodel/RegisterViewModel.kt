package com.gtech.rapidly.features.domain.auth.viewmodel

import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val goToDeliveryDashboard: () -> Unit,
    private val goToAdminDashboard: () -> Unit,
) : ScreenModel() {

    fun processRegistration(
        fullName: String,
        addharNumber: String,
        drivingLicense: String,
        vehicleNumber: String,
        phoneNumber: String,
        password: String
    ) = screenModelScope.launch {
        withContext(Dispatchers.Default) {

            val user = getUserAndValidate(
                fullName,
                addharNumber,
                drivingLicense,
                vehicleNumber,
                phoneNumber,
                password
            ) ?: return@withContext

            if (!createUserSession(user)) {
                showMessage("Failed to create user session, please try again")
                return@withContext
            }

            withContext(Dispatchers.Main) {
                if (user.userType == User.UserType.DELIVERY_BOY) {
                    goToDeliveryDashboard()
                } else {
                    goToAdminDashboard()
                }
            }

        }
    }

    private suspend fun getUserAndValidate(
        fullName: String,
        addharNumber: String,
        drivingLicense: String,
        vehicleNumber: String,
        phoneNumber: String,
        password: String
    ): User? {

        if (
            fullName.isEmpty() ||
            fullName.length < 3 ||
            fullName.length > 50
        ) {
            showMessage("Please enter a valid full name")
            return null
        }

        if (
            addharNumber.isEmpty() ||
            addharNumber.length < 12 ||
            addharNumber.length > 12 ||
            !addharNumber.matches(Regex("^[0-9]*$"))
        ) {
            showMessage("Please enter a valid Aadhar number")
            return null
        }

        if (
            drivingLicense.isEmpty() ||
            drivingLicense.length < 15 ||
            drivingLicense.length > 15
        ) {
            showMessage("Please enter a valid driving license number")
            return null
        }

        if (
            vehicleNumber.isEmpty() ||
            vehicleNumber.length < 10 ||
            vehicleNumber.length > 10
        ) {
            showMessage("Please enter a valid vehicle number")
            return null
        }

        if (
            phoneNumber.isEmpty() ||
            phoneNumber.length < 10 ||
            phoneNumber.length > 10 ||
            !phoneNumber.matches(Regex("^[0-9]*$"))
        ) {
            showMessage("Please enter a valid phone number")
            return null
        }

        if (
            password.isEmpty() ||
            password.length < 6 ||
            password.length > 20
        ) {
            showMessage("Please enter a valid password")
            return null
        }

        val user = User(
            name = fullName,
            addharCardNumber = addharNumber,
            drivingLicenceNumber = drivingLicense,
            vehicleNumber = vehicleNumber,
            phoneNumber = phoneNumber.toLong(),
            password = password,
            userType = User.UserType.DELIVERY_BOY
        )

        if (!UserService.saveOrUpdate(user)) {
            showMessage("Failed to register user, please try again")
            return null
        }

        return user

    }

}