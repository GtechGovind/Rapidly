package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.service.OrderService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PendingOrderViewModel: ViewModel() {

    var pendingOrders by mutableStateOf<List<Order>>(emptyList())

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.IO) {
            withLoading {
                pendingOrders = OrderService.getByStatus(
                    Order.Status.PICKUP,
                    RuntimeCache.getCurrentUser().phoneNumber
                )
            }
        }
    }

}