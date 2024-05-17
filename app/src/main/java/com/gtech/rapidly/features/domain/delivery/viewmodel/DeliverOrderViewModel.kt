package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.service.AccountingService
import com.gtech.rapidly.features.common.firestore.service.OrderService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.convert.round
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DeliverOrderViewModel(
    private val order: Order,
    private val goBack: () -> Unit
) : ScreenModel() {

    var deliveryNote by mutableStateOf(TextFieldValue())

    fun deliverOrder() = screenModelScope.launch(
        Dispatchers.IO
    ) {
        withLoading {
            try {
                if (processOrder()) {
                    showMessage("order delivered successfully")
                    withContext(Dispatchers.Main) {
                        goBack()
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun processOrder(): Boolean {

        val db = FirebaseFirestore.getInstance()
        val user = RuntimeCache.getCurrentUser()
        val settings = RuntimeCache.getSettings()
        val restaurant = RuntimeCache.getRestaurants().first { it.id == order.restaurantId }
        val accounting = AccountingService.getReport()
            ?: run {
                showMessage("failed to get accounting report, please try again")
                return false
            }

        val orderAmount = order.billAmount.round()
        val companyEarning = (orderAmount * restaurant.percentage).round()
        val deliveryBoyCommission = (companyEarning * settings.deliveryBoyPercentage).round()
        val companyCommission = (companyEarning - deliveryBoyCommission).round()

        // UPDATE ORDER
        order.orderStatus = Order.Status.DELIVERED
        order.updatedAt = Timestamp.now()
        order.deliveryTime = Timestamp.now()

        // UPDATE USER
        user.totalSalary += deliveryBoyCommission
        user.orderCount += 1

        // UPDATE ACCOUNTING
        accounting.totalOrderAmount += orderAmount
        accounting.totalCompanyEarning += companyEarning
        accounting.totalCompanyCommission += companyCommission
        accounting.totalDeliveryBoyCommission += deliveryBoyCommission

        // UPDATE DATABASE
        val result = db.runTransaction {
            runBlocking {
                if (!UserService.saveOrUpdate(user)) throw Exception("failed to update user")
                if (!OrderService.saveOrUpdate(order)) throw Exception("failed to update order")
                if (!AccountingService.saveOrUpdate(accounting)) throw Exception("failed to update accounting")
            }
        }.await()

        if (result == null) {
            showMessage("failed to process order, please try again")
            return false
        }

        return true

    }

}