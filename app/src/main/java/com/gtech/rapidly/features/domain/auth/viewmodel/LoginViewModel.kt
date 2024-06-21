package com.gtech.rapidly.features.domain.auth.viewmodel

import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.BuildConfig
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.SettingsService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val goToDeliveryDashboard:() -> Unit,
    private val goToAdminDashboard:() -> Unit,
) : ScreenModel() {

    fun processLogin(
        phoneNumber: String,
        password: String
    ) = screenModelScope.launch {
        withContext(Dispatchers.Default) {
            withLoading {
                val user = getAndValidateUser(phoneNumber, password) ?: return@withLoading
                if (!createUserSession(user)) {
                    showMessage("Failed to create user session, please try again")
                    return@withLoading
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
    }

    private suspend fun getAndValidateUser(
        phoneNumber: String,
        password: String
    ): User? {

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


        val user = UserService.getUserByPhoneNumber(phoneNumber)
        if (user == null) {
            showMessage("User not found, please register first")
            return null
        }

        if (user.password != password) {
            showMessage("Password did not match, please try again")
            return null
        }

        if (user.status != User.Status.ACTIVE) {
            showMessage("User account is not active, please contact admin!")
            return null
        }

        val setting = SettingsService.getSetting()
        if (setting == null) {
            showMessage("Failed to load settings, please try again!")
            return null
        }

        if (!BuildConfig.DEBUG) {
            if (
                setting.checkVersion &&
                BuildConfig.VERSION_NAME != setting.applicationVersion
            ) {
                showMessage("App is outdated, please update the app!")
                return null
            }
        }

        return user

    }

}