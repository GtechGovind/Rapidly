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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.ui.theme.RapidlyTheme
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.utils.convert.round
import com.gtech.rapidly.utils.misc.GTime
import com.gtech.rapidly.utils.misc.GTime.diffInMinute
import com.gtech.rapidly.utils.misc.GTime.toTime

@Composable
fun PendingOrderItem(
    order: Order,
    modifier: Modifier,
    onCallCustomer: (phoneNumber: String) -> Unit,
    onOrderDelivery: (order: Order) -> Unit,
    onCopyToClipBoard: (text: String) -> Unit,
    onDeleteOrder: (order: Order) -> Unit
) {

    OutlinedCard(
        modifier = modifier,
        onClick = {
            onOrderDelivery(order)
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bill No", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Time", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Order Status", style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.billNo, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.pickupTime?.toTime("hh:mm:ss") ?: "NA",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = if (order.pickupTime != null) {
                            if (order.pickupTime.diffInMinute() < 30) "On Time"
                            else order.pickupTime.diffInMinute().toString() + " Min Late"
                        } else "NA", style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                onClick = { onDeleteOrder(order) }
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
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Order",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Delete Order", fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = { onCallCustomer(order.customerNumber.toString()) }
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
                        text = order.customerNumber.toString(), fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(
    order: Order, modifier: Modifier
) {

    OutlinedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                        text = "Bill No", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Time", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Delivered Time", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Total Time Taken", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Customer Number", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Pickup Note", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Delivery Note", style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.billNo, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.pickupTime?.toTime("hh:mm:ss") ?: "NA",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.deliveryTime?.toTime("hh:mm:ss") ?: "NA",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.pickupTime?.diffInMinute().toString() + " Min",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.customerNumber.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.pickupNote, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = order.deliveryNote, style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun WithdrawItem(
    modifier: Modifier, item: Withdraw
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
                modifier = Modifier.fillMaxWidth(.8f),
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
                    if (item.attendedBy != 0L) {
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
                            text = item.attendeeNote,
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
                .padding(16.dp), item = Withdraw(
                requestAmount = 100.0,
                requestNote = "Test",
                attendedBy = 2345678,
                attendeeNote = "tfygujdsbchdhsbcuhdvcgvdsuchugdvcgvds",
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
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                PendingOrderItem(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), order = Order(
                    orderId = "SDFGHJKJHGFGHJKLKJHGFDFGH",
                    customerNumber = 1234567890,
                ), onCallCustomer = {

                }, onOrderDelivery = {

                }, onCopyToClipBoard = {

                }, onDeleteOrder = {})
            }
        }
    }
}

@Composable
@Preview
private fun OrderHistoryItemPreview() {
    RapidlyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                OrderHistoryItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), order = Order(
                        orderId = "SDFGHJKJHGFGHJKLKJHGFDFGH",
                        customerNumber = 1234567890,
                    )
                )
            }
        }
    }
}

@Composable
fun ModifyDeliveryBoyItem(
    modifier: Modifier,
    user: User,
    isLoading: Boolean,
    onToggleUserStatus: (user: User) -> Unit,
    onWithdraw: (user: User) -> Unit,
    onPenalty: (user: User) -> Unit
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = user.name.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
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
                        text = "Phone Number", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "DL NUmber", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Vehicle Number", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Order Count", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Password", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Total Income", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Total Penalty", style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Total Withdrawal", style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.phoneNumber.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = user.drivingLicenceNumber.uppercase(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = user.vehicleNumber.uppercase(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = user.orderCount.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = user.password, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "₹ ${user.totalSalary.round()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "₹ ${user.totalPenalties.round()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "₹ ${user.totalWithdrawal.round()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.clickable(
                        enabled = !isLoading
                    ) {
                        onToggleUserStatus(user)
                    },
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .padding(start = 16.dp),
                        painter = if (user.status == User.Status.ACTIVE) {
                            painterResource(R.drawable.activate_user)
                        } else {
                            painterResource(R.drawable.deactivate_user)
                        },
                        contentDescription = "User Profile",
                    )
                }

                Box(
                    modifier = Modifier.clickable(
                        enabled = !isLoading
                    ) {
                        onWithdraw(user)
                    },
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        painter = painterResource(R.drawable.withdrawal),
                        contentDescription = "User Withdrawal",
                    )
                }

                Box(
                    modifier = Modifier.clickable(
                        enabled = !isLoading
                    ) {
                        onPenalty(user)
                    },
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .padding(end = 16.dp),
                        painter = painterResource(R.drawable.penalty),
                        contentDescription = "Penalty",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ModifyDeliveryBoyItemPreview() {
    WithTheme {
        ModifyDeliveryBoyItem(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
            user = User(name = "John Doe"),
            false,
            {},
            {},
            {})
    }
}