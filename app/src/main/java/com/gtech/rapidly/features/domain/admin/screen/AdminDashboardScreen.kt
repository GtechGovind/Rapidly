package com.gtech.rapidly.features.domain.admin.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminDashboardViewModel
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminMainViewModel
import com.gtech.rapidly.utils.convert.round
import com.gtech.rapidly.utils.misc.RuntimeCache

object AdminDashboardScreen : Screen {

    private fun readResolve(): Any = AdminDashboardScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            AdminDashboardViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: AdminDashboardViewModel
    ) {

        if (
            RuntimeCache
                .getCurrentUser()
                .hasPermission(User.Permission.VIEW_COMPANY_STATISTICS)
        ) {
            CompanyStatsView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (
            RuntimeCache
                .getCurrentUser()
                .hasPermission(User.Permission.VIEW_DELIVERY_BOY_STATISTICS)
        ) {
            DeliveryBoyStatsView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }


    }

    @Composable
    private fun CompanyStatsView(
        modifier: Modifier, viewModel: AdminDashboardViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {

            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                text = "COMPANY STATS",
                style = MaterialTheme.typography.bodySmall,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                StatsCard(
                    modifier = Modifier.weight(1f),
                    title = "Company\nCommission",
                    value = "₹ ${viewModel.reports?.totalCompanyCommission?.round() ?: "?"}"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatsCard(
                    modifier = Modifier.weight(1f),
                    title = "Delivery\nCommission",
                    value = "₹ ${viewModel.reports?.totalDeliveryBoyCommission?.round() ?: "?"}"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                StatsCard(
                    modifier = Modifier.weight(1f),
                    title = "Order\nAmount",
                    value = "₹ ${viewModel.reports?.totalOrderAmount?.round() ?: "?"}"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatsCard(
                    modifier = Modifier.weight(1f),
                    title = "Company\nEarnings",
                    value = "₹ ${viewModel.reports?.totalCompanyEarning?.round() ?: "?"}"
                )
            }

        }
    }

    @Composable
    private fun DeliveryBoyStatsView(
        modifier: Modifier, viewModel: AdminDashboardViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {

            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                text = "DELIVERY BOY STATS",
                style = MaterialTheme.typography.bodySmall,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                StatsCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navigator.push(AdminDeliveryBoyListScreen)
                        },
                    title = "Delivery\nBoys",
                    value = "${viewModel.users.filter { it.userType == User.UserType.DELIVERY_BOY }.size}"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatsCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navigator.push(AdminWithdrawalScreen)
                        },
                    title = "Withdrawal\nRequests",
                    value = "${
                        AdminMainViewModel
                            .instance
                            ?.withdrawNotification
                            ?: 0
                    }"
                )
            }
        }
    }

    @Composable
    private fun StatsCard(
        modifier: Modifier, title: String, value: String
    ) {
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    /*@Composable
    private fun UsersView(
        modifier: Modifier, viewModel: AdminDashboardViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(viewModel.users) {
                    UserCard(
                        modifier = Modifier,
                        user = it,
                        toggleUserStatus = { user ->
                            if (!viewModel.isLoading) {
                                viewModel.toggleUserStatus(user)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

    @Composable
    private fun UserCard(
        modifier: Modifier,
        user: User,
        toggleUserStatus: (User) -> Unit,
    ) {
        Card(
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = user.name, style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₹ ${user.totalSalary.round()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row {
                    Image(
                        modifier = Modifier.clickable {
                            toggleUserStatus(user)
                        },
                        painter = if (user.status == User.Status.ACTIVE) {
                            painterResource(id = R.drawable.deactivate_user)
                        } else {
                            painterResource(id = R.drawable.activate_user)
                        },
                        contentDescription = "withdrawal money"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        modifier = Modifier.clickable {
                            //navigator.push(UserDetailsScreen(user))
                        },
                        painter = painterResource(id = R.drawable.penalty),
                        contentDescription = "add penalty"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        modifier = Modifier.clickable {
                            //navigator.push(UserDetailsScreen(user))
                        },
                        painter = painterResource(id = R.drawable.withdrawal),
                        contentDescription = "withdrawal money"
                    )
                }
            }
        }
    }*/

}

@Composable
@Preview
private fun Preview() {
    RuntimeCache.saveCurrentUser(User().apply {
        permissions = listOf(
            User.Permission.VIEW_COMPANY_STATISTICS,
            User.Permission.VIEW_DELIVERY_BOY_STATISTICS
        )
    })
    WithTheme {
        AdminDashboardScreen
            .View(AdminDashboardViewModel())
    }
}