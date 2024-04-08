package com.gtech.rapidly.features.domain.delivery.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.firestore.model.Order
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.components.NavBar
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.delivery.viewmodel.DeliverOrderViewModel
import com.gtech.rapidly.features.domain.user.UserProfileScreen
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

data class DeliverOrderScreen(
    val order: Order
) : Screen {

    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel: DeliverOrderViewModel = viewModel { DeliverOrderViewModel(order) }
        SubscribeToLifecycle(viewModel)
        when (viewModel.navigationEvent) {
            is DeliverOrderViewModel.NavigationEvent.GoBack -> navigator.pop()
            else -> Unit
        }
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: DeliverOrderViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            var signatureAdapter: SignaturePadAdapter? = null

            NavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = "PENDING ORDER",
                onBack = {
                    navigator.pop()
                },
                onProfile = {
                    navigator.push(UserProfileScreen)
                }
            )

            OutlinedCard(
                modifier = Modifier
                    .height(500.dp)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Signature",
                        style = MaterialTheme.typography.bodySmall
                    )

                    SignaturePadView(
                        onReady = { adapter ->
                            signatureAdapter = adapter
                        },
                        penColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Image(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .clickable {
                                signatureAdapter?.clear()
                            }
                            .padding(16.dp),
                        painter = painterResource(id = R.drawable.retry),
                        contentDescription = "Retry"
                    )

                }
            }

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isLoading = viewModel.isLoading,
                text = "DELIVER ORDER"
            ) {
                viewModel.deliverOrder()
            }

        }
    }
}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        DeliverOrderScreen(
            order = Order()
        ).View(DeliverOrderViewModel(Order()))
    }
}