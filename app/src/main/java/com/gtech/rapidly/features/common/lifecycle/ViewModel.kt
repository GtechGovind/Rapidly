package com.gtech.rapidly.features.common.lifecycle

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.gtech.rapidly.app.RapidlyApp
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.utils.misc.GResource
import com.gtech.rapidly.utils.misc.RuntimeCache
import com.gtech.rapidly.utils.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

typealias AndroidViewModel = androidx.lifecycle.ViewModel

abstract class ViewModel : AndroidViewModel() {

    var isLoading by mutableStateOf(false)
        protected set

    fun init() = viewModelScope.launch { onCreated() }
    fun destroy() = onCleared()

    open suspend fun onCreated() {

    }

    open fun onDestroy() {

    }

    override fun onCleared() {
        onDestroy()
        super.onCleared()
    }

    suspend fun withLoading(
        context: CoroutineContext = Dispatchers.Default,
        block: suspend () -> Unit
    ) {
        withContext(Dispatchers.Main) { isLoading = true }
        try {
            withContext(context) { block() }
        } catch (e: Exception) {
            handleError(e)
        } finally {
            withContext(Dispatchers.Main) { isLoading = false }
        }
    }

    suspend fun showMessage(
        message: String
    ) = withContext(Dispatchers.Main) {
        Toast.makeText(
            RapidlyApp.instance,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    suspend fun handleError(
        error: Throwable
    ) = withContext(Dispatchers.Main) {
        error(error)
        showMessage(error.message ?: "Unhandled error occurred!")
    }

    suspend fun createUserSession(user: User): Boolean {
        val userSession = "${user.phoneNumber}|${System.currentTimeMillis()}"
        GResource.saveData(GResource.Key.USER_SESSION, userSession)
        GResource.saveData(GResource.Key.IS_LOGIN, true)
        RuntimeCache.saveCurrentUser(user)
        return RuntimeCache.initResources()
    }

    suspend fun destroyUserSession() {
        GResource.saveData(GResource.Key.IS_LOGIN, false)
        GResource.saveData(GResource.Key.USER_SESSION, "")
    }

}