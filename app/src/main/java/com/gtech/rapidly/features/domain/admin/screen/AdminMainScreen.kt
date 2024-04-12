package com.gtech.rapidly.features.domain.admin.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.R
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminMainViewModel
import com.gtech.rapidly.features.domain.delivery.screen.DeliveryBoyProfileScreen

object AdminMainScreen : Screen {

    private lateinit var navigator: Navigator
    private fun readResolve(): Any = AdminMainScreen

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.rememberNavigatorScreenModel {
            AdminMainViewModel()
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: AdminMainViewModel
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(16.dp),
                viewModel = viewModel
            )
            Navigator(AdminDashboardScreen)
        }
    }

    @Composable
    private fun NavBar(
        modifier: Modifier,
        viewModel: AdminMainViewModel
    ) {
        ElevatedCard(
            modifier = modifier,
            shape = MaterialTheme.shapes.small,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp),
                    text = viewModel.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {
                                navigator.push(DeliveryBoyProfileScreen)
                            }
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp),
                            painter = painterResource(R.drawable.user),
                            contentDescription = "Logout",
                        )
                    }
                }
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        AdminMainScreen.View(
            AdminMainViewModel()
        )
    }
}