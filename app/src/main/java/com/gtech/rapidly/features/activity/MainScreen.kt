package com.gtech.rapidly.features.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.domain.admin.screen.AdminMainScreen
import com.gtech.rapidly.features.domain.auth.screen.LoginScreen
import com.gtech.rapidly.features.domain.delivery.screen.DeliveryMainScreen

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            MainViewModel(
                goToLogin = { navigator.replaceAll(LoginScreen) },
                goToDeliveryDashboard = { navigator.replaceAll(DeliveryMainScreen) },
                goToAdminDashboard = { navigator.replaceAll(AdminMainScreen) }
            )
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    private fun View(
        viewModel: MainViewModel
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.errorMessage == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = viewModel.errorMessage.toString(),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            viewModel.retry()
                        }
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }

}