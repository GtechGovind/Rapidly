package com.gtech.rapidly.features.domain.delivery.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.domain.auth.screen.LoginScreen
import com.gtech.rapidly.features.domain.delivery.viewmodel.DeliveryBoyProfileViewModel
import com.gtech.rapidly.features.domain.terms.TermsAndConditionScreen
import com.gtech.rapidly.utils.convert.round

object DeliveryBoyProfileScreen : Screen {

    private fun readResolve(): Any = TermsAndConditionScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            DeliveryBoyProfileViewModel(
                goToLogin = {
                    navigator.replaceAll(LoginScreen)
                }
            )
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(viewModel: DeliveryBoyProfileViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.profile)
            )

            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            ProfileView(
                modifier = Modifier
                    .fillMaxSize(),
                viewModel = viewModel,
            )

        }
    }

    @Composable
    private fun ProfileView(
        modifier: Modifier,
        viewModel: DeliveryBoyProfileViewModel,
    ) {
        ElevatedCard(
            modifier = modifier,
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = viewModel.user?.name?.uppercase() ?: "Loading...",
                    style = MaterialTheme.typography.displaySmall
                )

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Earnings are based on per order mode, you will get some percentage of order amount. Penalties include late fees and violation of company's code of conduct.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Card(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "₹ ${viewModel.user?.totalSalary?.round() ?: "?"}",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "Earnings",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "₹ ${viewModel.user?.totalPenalties?.round() ?: "?"}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = "Penalties",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "₹ ${viewModel.user?.totalWithdrawal?.round() ?: "?"}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = "Withdrawals",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "★ 3.4",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = "Rating",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${viewModel.user?.orderCount ?: "?"}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = "Orders",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                LoadingButton(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                    isLoading = viewModel.isLoading,
                    text = "Withdraw Funds",
                ) {
                    navigator.push(DeliveryWithdrawScreen)
                }

                LoadingButton(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    isLoading = viewModel.isLoading,
                    text = "Logout",
                ) {
                    viewModel.logout()
                }

            }

        }

    }

}