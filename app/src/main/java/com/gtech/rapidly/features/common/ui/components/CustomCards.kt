package com.gtech.rapidly.features.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.ui.theme.RapidlyTheme
import com.gtech.rapidly.utils.misc.GTime

@Composable
fun PendingOrderItem(
    order: Order,
    modifier: Modifier,
    onCallCustomer: (phoneNumber: String) -> Unit,
    onOrderDelivery: (order: Order) -> Unit,
    onCopyToClipBoard: (text: String) -> Unit
) {

    OutlinedCard(
        modifier = modifier,
        onClick = {
            onOrderDelivery(order)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onCopyToClipBoard(order.orderId)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.pending_order),
                    contentDescription = "Order Id ${order.orderId}"
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = order.orderId,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bill No",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Time",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Order Status",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.billNo,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = GTime.toTime(order.pickupTime, "hh:mm:ss"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = if (GTime.getDiffInMinute(order.pickupTime) < 30) "On Time"
                        else GTime.getDiffInMinute(order.pickupTime).toString() + " Min Late",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                onClick = {
                    onCallCustomer(order.customerNumber.toString())
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call Customer",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.customerNumber.toString(),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(
    order: Order,
    modifier: Modifier
) {

    OutlinedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.order_history),
                    contentDescription = "Order Id ${order.orderId}"
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = order.orderId,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bill No",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Time",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Delivered Time",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Total Time Taken",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Customer Number",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Note",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Delivery Note",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.billNo,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = GTime.toTime(order.pickupTime, "hh:mm:ss"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = GTime.toTime(order.deliveryTime, "hh:mm:ss"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = GTime.diffInMinute(order.pickupTime, order.deliveryTime).toString() + " Min",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.customerNumber.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.pickupNote,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.deliveryNote,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PendingOrderItemPreview() {
    RapidlyTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                PendingOrderItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    order = Order(
                        orderId = "SDFGHJKJHGFGHJKLKJHGFDFGH",
                        customerNumber = 1234567890,
                    ),
                    onCallCustomer = {

                    },
                    onOrderDelivery = {

                    },
                    onCopyToClipBoard = {

                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun OrderHistoryItemPreview() {
    RapidlyTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                OrderHistoryItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    order = Order(
                        orderId = "SDFGHJKJHGFGHJKLKJHGFDFGH",
                        customerNumber = 1234567890,
                    )
                )
            }
        }
    }
}