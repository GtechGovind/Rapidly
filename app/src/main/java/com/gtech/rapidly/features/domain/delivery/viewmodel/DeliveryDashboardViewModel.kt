package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeliveryDashboardViewModel: ScreenModel() {

    var restaurants by mutableStateOf<List<Restaurant>>(emptyList())

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.Default) {
            withLoading {
                restaurants = RuntimeCache.getRestaurants()
            }
        }
    }

}