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
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.ui.theme.RapidlyTheme
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.utils.misc.GTime
import com.gtech.rapidly.utils.misc.GTime.toTime

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
                        text = GTime.diffInMinute(order.deliveryTime, order.pickupTime)
                            .toString() + " Min",
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
fun WithdrawItem(
    modifier: Modifier,
    item: Withdraw
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier
                    .align(Alignment.End)
                    .fillMaxWidth(.8f),
                shape = MaterialTheme.shapes.medium,
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "₹ ${item.requestAmount}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = item.requestNote,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = item.createdAt.toTime("dd-MM-yyyy hh:mm:ss"),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(.8f),
                shape = MaterialTheme.shapes.medium,
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (item.approvedBy != 0L) {
                        Text(
                            text = "₹ ${item.approvedAmount} | ${item.status.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = "TID: ${item.transactionId}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = item.approvedNote,
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = item.updatedAt?.toTime("dd-MM-yyyy hh:mm:ss") ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    } else {
                        Text(
                            text = "Waiting for Approval",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun WithdrawItemPreview() {
    WithTheme {
        WithdrawItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            item = Withdraw(
                requestAmount = 100.0,
                requestNote = "Test",
                approvedBy = 2345678,
                approvedNote = "tfygujdsbchdhsbcuhdvcgvdsuchugdvcgvds",
                transactionId = "3456789oiuyghfvbn".uppercase(),
                status = Withdraw.Status.APPROVED,
                updatedAt = Timestamp.now()
            )
        )
    }
}

@Composable
@Preview
private fun PendingOrderItemPreview() {
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
private fun OrderHistoryItemPreview() {
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