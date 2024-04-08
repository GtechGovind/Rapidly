package com.gtech.rapidly.features.domain.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {

    var user by mutableStateOf<User?>(null)
    var navigationEvent: NavigationEvent? by mutableStateOf(null)
        private set

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.IO) {
            withLoading {
                user = UserService.getUserByPhoneNumber(
                    RuntimeCache.getCurrentUser().phoneNumber.toString()
                )
                if (user == null) user = RuntimeCache.getCurrentUser()
                else RuntimeCache.saveCurrentUser(user!!)
            }
        }
    }

    fun logout() = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            destroyUserSession()
            navigate(NavigationEvent.Login)
        }
    }

    private suspend fun navigate(
        event: NavigationEvent
    ) = withContext(Dispatchers.Main) {
        navigationEvent = event
    }

    sealed class NavigationEvent {
        data object Login : NavigationEvent()
    }

}