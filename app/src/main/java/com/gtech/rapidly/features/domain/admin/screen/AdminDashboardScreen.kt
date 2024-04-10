package com.gtech.rapidly.features.domain.admin.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.ui.components.AdminNavBar
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminDashboardViewModel
import com.gtech.rapidly.features.domain.auth.screen.LoginScreen
import com.gtech.rapidly.utils.convert.round

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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AdminNavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                title = "Dashboard",
            ) {
                navigator.replaceAll(LoginScreen)
            }

            CompanyStatsView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )

            UsersView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), viewModel = viewModel
            )

        }

        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }

    @Composable
    private fun CompanyStatsView(
        modifier: Modifier, viewModel: AdminDashboardViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
        ) {

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

    @Composable
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
    }

}

@Composable
@Preview
fun AdminDashboardScreenPreview() {
    WithTheme {
        AdminDashboardScreen
            .View(AdminDashboardViewModel())
    }
}