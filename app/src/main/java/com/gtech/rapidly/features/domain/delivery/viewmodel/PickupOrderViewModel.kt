package com.gtech.rapidly.features.domain.delivery.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.firestore.service.OrderService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import com.gtech.rapidly.features.domain.delivery.service.ImageToTextService
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PickupOrderViewModel : ViewModel() {

    var scannedOrder by mutableStateOf<Uri>(Uri.EMPTY)
    var restaurants: List<Restaurant> by mutableStateOf(emptyList())
    var selectedRestaurant by mutableStateOf(TextFieldValue())
    var billNumber by mutableStateOf(TextFieldValue())
    var billNumberEnableStatus by mutableStateOf(false)
    var amount by mutableStateOf(TextFieldValue())
    var customerNumber by mutableStateOf(TextFieldValue())

    var navigationEvent: NavigationEvent? by mutableStateOf(null)
        private set

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.IO) {
            restaurants = RuntimeCache.getRestaurants()
        }
    }

    fun processImage(
        imageUri: Uri
    ) = viewModelScope.launch(Dispatchers.Default) {
        val result = ImageToTextService.processImage(imageUri)
        if (result.isNotBlank()) {
            val hotelBill = ImageToTextService.parseHotelBill(result)
            selectedRestaurant = TextFieldValue(hotelBill.restaurant?.name ?: "")
            if (hotelBill.billNumber != null) {
                billNumber = TextFieldValue(hotelBill.billNumber.toString())
                billNumberEnableStatus = false
            } else {
                billNumberEnableStatus = true
            }
            amount = TextFieldValue(hotelBill.amount?.toString() ?: "")
        } else {
            showMessage("Failed to process image, please try again")
        }
    }

    fun processOrder() = viewModelScope.launch(
        Dispatchers.Default
    ) {
        withLoading {
            val order = getOrderAndValidate() ?: return@withLoading
            if (OrderService.saveOrUpdate(order)) {
                showMessage("Order added successfully.")
                navigate(NavigationEvent.GoBack)
            } else {
                showMessage("Failed to add order.")
            }
        }
    }

    private suspend fun getOrderAndValidate(): Order? {

        val restaurantName = selectedRestaurant.text
        val customerNumber = customerNumber.text
        val billNumber = billNumber.text
        val amount = amount.text

        if (restaurantName.isEmpty()) {
            showMessage("Please select restaurant")
            return null
        }

        if (customerNumber.isEmpty()) {
            showMessage("Please enter customer number")
            return null
        }

        if (billNumber.isEmpty()) {
            showMessage("Please enter bill number")
            return null
        }

        if (amount.isEmpty()) {
            showMessage("Please enter amount")
            return null
        }

        val restaurant = restaurants.find { it.name == selectedRestaurant.text }
        if (restaurant == null) {
            showMessage("Invalid restaurant, please check")
            return null
        }

        val user = RuntimeCache.getCurrentUser()

        val orderId = Order.genOrderId(
            billNumber = billNumber,
            deliveryBoyNumber = user.phoneNumber
        )

        val billNoHash = Order.genBillNoHash(
            billNumber = billNumber,
            amount = amount.toDouble()
        )

        if (OrderService.getByBillNoHash(billNoHash) != null) {
            showMessage("Order already exists, please check")
            return null
        }

        return Order(
            orderId = orderId,
            billNo = billNumber,
            billNoHash = billNoHash,
            billAmount = amount.toDouble(),
            restaurantId = restaurant.id,
            deliveryNote = "",
            pickupNote = "",
            deliveryBoyNumber = user.phoneNumber,
            pickupTime = System.currentTimeMillis(),
            customerNumber = customerNumber.toLong(),
            orderStatus = Order.Status.PICKUP,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )

    }

    private suspend fun navigate(
        newEvent: NavigationEvent?
    ) = withContext(Dispatchers.Main) {
        navigationEvent = newEvent
    }

    override fun onDestroy() {
        runBlocking {
            navigate(null)
        }
        super.onDestroy()
    }

    sealed class NavigationEvent {
        data object GoBack : NavigationEvent()
    }

}