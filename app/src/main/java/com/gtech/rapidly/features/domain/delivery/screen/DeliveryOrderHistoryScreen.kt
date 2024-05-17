package com.gtech.rapidly.features.domain.delivery.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.ui.components.OrderHistoryItem
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.delivery.viewmodel.DeliveryOrderHistoryViewModel

object DeliveryOrderHistoryScreen : Screen {

    private fun readResolve(): Any = DeliveryOrderHistoryScreen

    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            DeliveryOrderHistoryViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: DeliveryOrderHistoryViewModel
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                OrderHistoryView(
                    modifier = Modifier
                        .fillMaxSize(),
                    pendingOrders = viewModel.orderHistory
                )
            }
        }
    }

    @Composable
    private fun OrderHistoryView(
        modifier: Modifier,
        pendingOrders: List<Order>
    ) {

        if (pendingOrders.isEmpty()) {
            Box(modifier = modifier) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    text = "No Order History",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(pendingOrders) { order ->
                    OrderHistoryItem(
                        order = order,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        DeliveryOrderHistoryScreen.View(
            viewModel = DeliveryOrderHistoryViewModel()
                .apply {
                    orderHistory = listOf(
                        Order(
                            orderId = "123456",
                            billNo = "123456",
                            billNoHash = "123456",
                            billAmount = 123.0,
                            restaurantId = 123,
                            deliveryNote = "Delivery Note",
                            pickupNote = "Pickup Note",
                            deliveryBoyNumber = 123,
                            pickupTime = null,
                            deliveryTime = null,
                            pickupLocation = 123,
                            deliveryLocation = 123,
                            customerNumber = 123,
                            orderStatus = Order.Status.PICKUP,
                            scannedText = "Scanned Text",
                            createdAt = com.google.firebase.Timestamp.now(),
                            updatedAt = null
                        )
                    )
                }
        )
    }
}