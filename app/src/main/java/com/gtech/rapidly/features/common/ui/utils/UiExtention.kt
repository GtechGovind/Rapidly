package com.gtech.rapidly.features.common.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.features.common.ui.theme.RapidlyTheme

@Composable
fun WithTheme(
    content: @Composable () -> Unit
) = RapidlyTheme {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
fun Screen.SubscribeToLifecycle(
    viewModel: ScreenModel
) {
    LifecycleEffect(
        onStarted = { viewModel.init() }
    )
}

@Composable
fun WithLoading(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        content()
    }
}
