package com.gtech.rapidly.features.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.domain.admin.screen.AdminDashboardScreen
import com.gtech.rapidly.features.domain.auth.screen.LoginScreen
import com.gtech.rapidly.features.domain.delivery.screen.DeliveryDashboardScreen

class MainScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val viewModel = viewModel<MainViewModel>()

        SubscribeToLifecycle(viewModel)

        when (viewModel.navigationEvent) {
            is MainViewModel.NavigationEvent.Login -> navigator.replaceAll(LoginScreen)
            is MainViewModel.NavigationEvent.AdminDashboard -> navigator.replaceAll(AdminDashboardScreen)
            is MainViewModel.NavigationEvent.DeliveryDashboard -> navigator.replaceAll(DeliveryDashboardScreen)
            else -> {}
        }

        View()

    }

    @Composable
    private fun View() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }

}