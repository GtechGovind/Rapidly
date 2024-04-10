package com.gtech.rapidly.features.domain.delivery.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.components.VisibilityTracker
import com.gtech.rapidly.features.common.ui.components.WithdrawItem
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.delivery.viewmodel.FundWithdrawViewModel
import com.gtech.rapidly.features.domain.delivery.viewmodel.MainDeliveryViewModel

object FundWithdrawScreen : Screen {
    private fun readResolve(): Any = FundWithdrawScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            FundWithdrawViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: FundWithdrawViewModel
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            SubmitWithdrawRequestView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                viewModel = viewModel
            )

            WithdrawalRequestView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )

        }
    }

    @Composable
    private fun SubmitWithdrawRequestView(
        modifier: Modifier,
        viewModel: FundWithdrawViewModel
    ) {
        ElevatedCard(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "WITHDRAW REQUEST",
                    style = MaterialTheme.typography.bodyMedium,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.amount,
                    onValueChange = { viewModel.amount = it },
                    enabled = !viewModel.isLoading,
                    label = { Text("Amount") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Amount"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.note,
                    onValueChange = { viewModel.note = it },
                    enabled = !viewModel.isLoading,
                    label = { Text("Note ...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Note ..."
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
                        .padding(top = 16.dp),
                    isLoading = viewModel.isLoading,
                    text = "Submit Request",
                ) {
                    viewModel.submit()
                }

            }
        }
    }

    @Composable
    private fun WithdrawalRequestView(
        modifier: Modifier,
        viewModel: FundWithdrawViewModel
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
                    MainDeliveryViewModel.instance?.withdrawals ?: emptyList()
                ) {
                    if (it.isSeen.not()) {
                        VisibilityTracker(
                            threshold = 0.5f,
                            onVisibilityChanged = { isVisible ->
                                if (isVisible) {
                                    viewModel.markAsSeen(it)
                                }
                            }
                        ) {
                            WithdrawItem(
                                modifier = Modifier.fillMaxWidth(),
                                item = it
                            )
                        }
                    } else {
                        WithdrawItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = it
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        FundWithdrawScreen.View(
            viewModel = FundWithdrawViewModel().apply {
            }
        )
    }
}