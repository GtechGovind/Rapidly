package com.gtech.rapidly.features.domain.delivery.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.R
import com.gtech.rapidly.app.RapidlyApp
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.features.common.ui.components.ImageFromUrl
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.delivery.viewmodel.DeliveryDashboardViewModel

object DeliveryDashboardScreen : Screen {

    private fun readResolve(): Any = DeliveryDashboardScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            DeliveryDashboardViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: DeliveryDashboardViewModel
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            PartnerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                restaurants = viewModel.restaurants
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                shape = RectangleShape
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    OptionView(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(16.dp),
                        painter = painterResource(id = R.drawable.take_order),
                        title = "TAKE NEW ORDER",
                    ) {
                        navigator.push(PickupOrderScreen)
                    }

                    OptionView(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(16.dp),
                        painter = painterResource(id = R.drawable.pending_order),
                        title = "PENDING ORDER",
                    ) {
                        navigator.push(PendingOrderScreen)
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    OptionView(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(16.dp),
                        painter = painterResource(id = R.drawable.order_history),
                        title = "ORDER HISTORY",
                    ) {
                        navigator.push(OrderHistoryScreen)
                    }

                    OptionView(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(16.dp),
                        painter = painterResource(id = R.drawable.comming_soon),
                        title = "COMING SOON",
                    ) {
                        Toast.makeText(
                            RapidlyApp.instance,
                            "Coming Soon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }

        }

    }

    @Composable
    private fun PartnerView(
        modifier: Modifier,
        restaurants: List<Restaurant> = emptyList()
    ) {
        Box(
            modifier = modifier
        ) {

            /*Text(
                modifier = Modifier.align(Alignment.TopStart),
                text = "Our Partners",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
            )
*/
            LazyRow(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                items(restaurants) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageFromUrl(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .weight(0.8f),
                            imageUrl = it.logo
                        )
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun OptionView(
        modifier: Modifier,
        painter: Painter,
        title: String,
        isLoading: Boolean = false,
        onClick: () -> Unit
    ) {
        Card(
            onClick = onClick,
            modifier = modifier,
            enabled = !isLoading,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    painter = painter,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        DeliveryDashboardScreen.View(
            viewModel = DeliveryDashboardViewModel()
        )
    }
}
