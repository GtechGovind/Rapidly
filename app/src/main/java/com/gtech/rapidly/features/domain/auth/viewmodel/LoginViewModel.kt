package com.gtech.rapidly.features.domain.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    var navigationEvent: NavigationEvent? by mutableStateOf(null)
        private set

    fun processLogin(
        phoneNumber: String,
        password: String
    ) = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            withLoading {
                val user = getAndValidateUser(phoneNumber, password) ?: return@withLoading
                if (!createUserSession(user)) {
                    showMessage("Failed to create user session, please try again")
                    return@withLoading
                }
                navigate(
                    if (user.userType == User.UserType.DELIVERY_BOY) {
                        NavigationEvent.DeliveryDashboard
                    } else {
                        NavigationEvent.AdminDashboard
                    }
                )
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

        return user

    }

    private suspend fun navigate(
        newNavigationEvent: NavigationEvent
    ) = withContext(Dispatchers.Main) {
        navigationEvent = newNavigationEvent
    }

    sealed class NavigationEvent {
        data object Register : NavigationEvent()
        data object DeliveryDashboard : NavigationEvent()
        data object AdminDashboard : NavigationEvent()
    }

}