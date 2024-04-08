package com.gtech.rapidly.features.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gtech.rapidly.BuildConfig
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.SettingsService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import com.gtech.rapidly.utils.misc.GResource
import com.gtech.rapidly.utils.misc.GTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    var navigationEvent: NavigationEvent? by mutableStateOf(null)
        private set

    override suspend fun onCreated() {
        super.onCreated()

        val isLoggedIn = GResource.getBoolData(GResource.Key.IS_LOGIN)
        if (!isLoggedIn) {
            showMessage("Please login to continue!")
            navigate(NavigationEvent.Login)
            return
        }

        val userSession = GResource.getStringData(GResource.Key.USER_SESSION)
        if (userSession.isEmpty()) {
            showMessage("Please login to continue!")
            navigate(NavigationEvent.Login)
            return
        }

        val (phoneNumber: Long, loginTimeStamp: Long) = userSession
            .split("|")
            .map {
                it.trim()
                it.toLong()
            }

        val diffInMinute = GTime.getDiffInMinute(loginTimeStamp)
        if (diffInMinute > TimeUnit.DAYS.toMinutes(3)) {
            showMessage("User session expired, please login again!")
            navigate(NavigationEvent.Login)
            return
        }

        val user = UserService.getUserByPhoneNumber(phoneNumber.toString())
            ?: run {
                showMessage("User not found, please login again!")
                navigate(NavigationEvent.Login)
                return
            }

        if (user.status != User.Status.ACTIVE) {
            showMessage("User account is not active, please contact admin!")
            navigate(NavigationEvent.Login)
            return
        }

        if (!createUserSession(user)) {
            showMessage("Failed to create user session, please login again!")
            navigate(NavigationEvent.Login)
            return
        }

        val setting = SettingsService.getSetting()
        if (setting == null) {
            showMessage("Failed to load settings, please try again!")
            navigate(NavigationEvent.Login)
            return
        }

        if (BuildConfig.VERSION_NAME != setting.applicationVersion) {
            showMessage("App version mismatch, please update the app!")
            navigate(NavigationEvent.Login)
            return
        }

        showMessage("Welcome back, ${user.name}!")

        navigate(
            if (user.userType == User.UserType.DELIVERY_BOY) {
                NavigationEvent.DeliveryDashboard
            } else {
                NavigationEvent.AdminDashboard
            }
        )

    }

    private suspend fun navigate(
        newNavigationEvent: NavigationEvent
    ) = withContext(Dispatchers.Main) {
        navigationEvent = newNavigationEvent
    }

    sealed class NavigationEvent {
        data object Login : NavigationEvent()
        data object DeliveryDashboard : NavigationEvent()
        data object AdminDashboard : NavigationEvent()
    }

}