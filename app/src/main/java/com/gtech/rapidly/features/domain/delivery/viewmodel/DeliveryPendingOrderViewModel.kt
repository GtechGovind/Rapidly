package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.service.OrderService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryPendingOrderViewModel : ScreenModel() {

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

    fun deleteOrder(order: Order) = screenModelScope.launch(
        Dispatchers.IO
    ) {
        withLoading {
            if (OrderService.delete(order)) {
                showMessage("Order deleted successfully")
                pendingOrders = pendingOrders.filter { it.orderId != order.orderId }
            } else {
                showMessage("Failed to delete order, please try again")
            }
        }
    }

}