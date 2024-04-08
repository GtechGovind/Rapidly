package com.gtech.rapidly.features.domain.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val goToLogin: () -> Unit
) : ScreenModel() {

    var user by mutableStateOf<User?>(null)

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

    fun logout() = screenModelScope.launch {
        withContext(Dispatchers.Default) {
            destroyUserSession()
            withContext(Dispatchers.Main) {
                goToLogin()
            }
        }
    }

}