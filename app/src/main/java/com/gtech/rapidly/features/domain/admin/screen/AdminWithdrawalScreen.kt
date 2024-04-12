package com.gtech.rapidly.features.domain.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.firebase.Timestamp
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminMainViewModel
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminWithdrawalViewModel
import com.gtech.rapidly.utils.misc.GTime.toTime

object AdminWithdrawalScreen : Screen {
    private fun readResolve(): Any = AdminWithdrawalScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            AdminWithdrawalViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: AdminWithdrawalViewModel
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            PendingRequestsSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )

            WithdrawalRequestView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                viewModel = viewModel
            )

            if (viewModel.isProcessingDialogVisible) {
                ProcessWithdrawalDialog(viewModel)
            }

        }
    }

    @Composable
    private fun PendingRequestsSwitch(
        modifier: Modifier,
        viewModel: AdminWithdrawalViewModel
    ) {
        ElevatedCard(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Only Pending Requests",
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp),
                    checked = viewModel.showPendingRequests,
                    onCheckedChange = {
                        viewModel.showPendingRequests = it
                    }
                )
            }
        }
    }

    @Composable
    private fun WithdrawalRequestView(
        modifier: Modifier,
        viewModel: AdminWithdrawalViewModel
    ) {
        OutlinedCard(
            modifier = modifier
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "WITHDRAWALS",
                style = MaterialTheme.typography.bodyMedium,
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                items(
                    AdminMainViewModel.instance?.withdraws ?: emptyList()
                ) { it ->
                    if (it.first.status == Withdraw.Status.PENDING) {
                        PendingWithdrawItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            item = it,
                            viewModel = viewModel
                        )
                    } else {
                        if (!viewModel.showPendingRequests) {
                            WithdrawItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                item = it
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WithdrawItem(
        modifier: Modifier,
        item: Pair<Withdraw, User>
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: ${item.second.name} | ${item.second.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Date: " + item.first.createdAt.toTime("dd-MM-yyyy hh:mm:ss"),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Request Amount: ₹ ${item.first.requestAmount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Note: " + item.first.requestNote,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Text(
                    text = "Attended By: ${item.first.attendedBy}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Date: " + item.first.updatedAt?.toTime("dd-MM-yyyy hh:mm:ss"),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                if (item.first.status == Withdraw.Status.APPROVED) {
                    Text(
                        text = "Approved Amount: ₹ ${item.first.approvedAmount}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Transaction ID: ${item.first.transactionId}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                } else if (item.first.status == Withdraw.Status.REJECTED) {
                    Text(
                        text = "Rejected Reason: ${item.first.attendeeNote}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    @Composable
    fun PendingWithdrawItem(
        modifier: Modifier,
        item: Pair<Withdraw, User>,
        viewModel: AdminWithdrawalViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: ${item.second.name} | ${item.second.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Earning: ₹ ${item.second.totalSalary} | Withdrawn: ₹ ${item.second.totalWithdrawal}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Request Amount: ₹ ${item.first.requestAmount}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "Note: " + item.first.requestNote,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.first.createdAt.toTime("dd-MM-yyyy hh:mm:ss"),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            }

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isLoading = false,
                text = "Process",
            ) {
                viewModel.showProcessWithdrawalDialog(item)
            }

        }
    }

    @Composable
    private fun ProcessWithdrawalDialog(
        viewModel: AdminWithdrawalViewModel
    ) {
        Dialog(
            onDismissRequest = {
                viewModel.hideProcessWithdrawalDialog()
            }
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    var approvedAmount by remember { mutableStateOf(TextFieldValue("")) }
                    var transactionId by remember { mutableStateOf(TextFieldValue("")) }
                    var rejectedReason by remember { mutableStateOf(TextFieldValue("")) }
                    val isLoading = remember { mutableStateOf(false) }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = approvedAmount,
                        onValueChange = { approvedAmount = it },
                        enabled = !isLoading.value,
                        label = { Text("Approved Amount") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Approved Amount"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = transactionId,
                        onValueChange = { transactionId = it },
                        enabled = !isLoading.value,
                        label = { Text("Transaction ID") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Transaction ID"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    LoadingButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        isLoading = isLoading.value,
                        text = "Process To Pay",
                    ) {
                        viewModel.proceedToPay(
                            approvedAmount.text,
                            transactionId.text,
                            isLoading
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        value = rejectedReason,
                        onValueChange = { rejectedReason = it },
                        enabled = !isLoading.value,
                        label = { Text("Rejected Reason") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Rejected Reason"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    LoadingButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        isLoading = isLoading.value,
                        text = "Reject Request",
                    ) {
                        viewModel.rejectRequest(
                            rejectedReason.text,
                            isLoading
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PendingWithdrawItemPreview() {
    WithTheme {
        AdminWithdrawalScreen.PendingWithdrawItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            item = Pair(
                Withdraw(
                    requestAmount = 1000.0,
                    requestNote = "Test Withdrawal",
                    createdAt = Timestamp.now()
                ),
                User(
                    name = "Test User",
                    phoneNumber = 1234567890
                )
            ),
            viewModel = AdminWithdrawalViewModel()
        )
    }
}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        AdminMainViewModel().apply {
            withdraws.add(
                Pair(
                    Withdraw(
                        requestAmount = 1000.0,
                        requestNote = "Test Withdrawal",
                        createdAt = Timestamp.now()
                    ),
                    User(
                        name = "Test User",
                        phoneNumber = 1234567890
                    )
                )
            )
        }
        AdminWithdrawalScreen.View(AdminWithdrawalViewModel())
    }
}
