package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import com.gtech.rapidly.utils.misc.RuntimeCache

class DeliveryDashboardViewModel: ViewModel() {

    var restaurants by mutableStateOf<List<Restaurant>>(emptyList())

    override suspend fun onCreated() {
        super.onCreated()
        withLoading {
            restaurants = RuntimeCache.getRestaurants()
        }
    }

}