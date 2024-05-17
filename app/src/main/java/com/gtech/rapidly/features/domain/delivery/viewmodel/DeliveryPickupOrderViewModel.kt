package com.gtech.rapidly.features.domain.delivery.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.Timestamp
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.firestore.service.OrderService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.features.domain.delivery.service.ImageToTextService
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryPickupOrderViewModel(
    private val onBack: () -> Unit
) : ScreenModel() {

    var scannedOrder by mutableStateOf<Uri>(Uri.EMPTY)
    var restaurants: List<Restaurant> by mutableStateOf(emptyList())
    var selectedRestaurant by mutableStateOf(TextFieldValue())
    var billNumber by mutableStateOf(TextFieldValue())
    var amount by mutableStateOf(TextFieldValue())
    var customerNumber by mutableStateOf(TextFieldValue())
    var pickupNode by mutableStateOf(TextFieldValue())
    private var scannedText by mutableStateOf("")

    override suspend fun onCreated() {
        super.onCreated()
        withContext(Dispatchers.IO) {
            restaurants = RuntimeCache.getRestaurants()
        }
    }

    fun processImage(
        imageUri: Uri
    ) = screenModelScope.launch(Dispatchers.Default) {
        val result = ImageToTextService.processImage(imageUri)
        if (result.isNotBlank()) {
            scannedText = result
        } else {
            showMessage("Failed to process image, please try again")
        }
    }

    fun processOrder() = screenModelScope.launch(
        Dispatchers.Default
    ) {
        withLoading {
            val order = getOrderAndValidate() ?: return@withLoading
            if (OrderService.saveOrUpdate(order)) {
                showMessage("Order added successfully.")
                withContext(Dispatchers.Main) {
                    onBack()
                }
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

        if (scannedText.isEmpty() || scannedOrder == Uri.EMPTY) {
            showMessage("Please scan the hotel receipt!")
            return null
        }

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

        if (
            amount.isEmpty() ||
            amount.toDoubleOrNull() == null ||
            amount.toDouble() <= 0.0 ||
            amount.toDouble() > 20000.0
        ) {
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
            pickupNote = pickupNode.text,
            deliveryBoyNumber = user.phoneNumber,
            pickupTime = Timestamp.now(),
            customerNumber = customerNumber.toLong(),
            orderStatus = Order.Status.PICKUP,
            scannedText = scannedText,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )

    }

}