package com.gtech.rapidly.features.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.BuildConfig
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.SettingsService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.GResource
import com.gtech.rapidly.utils.misc.GTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val goToLogin: () -> Unit,
    private val goToDeliveryDashboard: () -> Unit,
    private val goToAdminDashboard: () -> Unit
) : ScreenModel() {

    var errorMessage: String? by mutableStateOf(null)

    override suspend fun onCreated() {
        super.onCreated()

        showError(null)

        val isLoggedIn = GResource.getBoolData(GResource.Key.IS_LOGIN)
        if (!isLoggedIn) {
            showMessage("Please login to continue!")
            navigate(goToLogin)
            return
        }

        val userSession = GResource.getStringData(GResource.Key.USER_SESSION)
        if (userSession.isEmpty()) {
            showMessage("Please login to continue!")
            navigate(goToLogin)
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
            navigate(goToLogin)
            return
        }

        val user = UserService.getUserByPhoneNumber(phoneNumber.toString())
            ?: run {
                showMessage("User not found, please login again!")
                navigate(goToLogin)
                return
            }

        if (user.status != User.Status.ACTIVE) {
            showError("User account is not active, please contact admin!")
            return
        }

        if (!createUserSession(user)) {
            showMessage("Failed to create user session, please login again!")
            navigate(goToLogin)
            return
        }

        val setting = SettingsService.getSetting()
        if (setting == null) {
            showError("Failed to load settings, please try again!")
            return
        }

        if (!BuildConfig.DEBUG) {
            if (
                setting.checkVersion &&
                BuildConfig.VERSION_NAME != setting.applicationVersion
            ) {
                showError("App is outdated, please update the app!")
                return
            }
        }

        showMessage("Welcome back, ${user.name}!")

        navigate(
            if (user.userType == User.UserType.DELIVERY_BOY) {
                goToDeliveryDashboard
            } else {
                goToAdminDashboard
            }
        )

    }

    fun retry() = screenModelScope.launch {
        onCreated()
    }

    private suspend fun navigate(
        block: suspend () -> Unit
    ) = withContext(Dispatchers.Main) {
        block()
    }

    private suspend fun showError(
        newMessage: String?
    ) = withLoading(Dispatchers.Main) {
        errorMessage = newMessage
    }

}