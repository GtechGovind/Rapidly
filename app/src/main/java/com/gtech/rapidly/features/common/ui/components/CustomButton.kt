package com.gtech.rapidly.features.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gtech.rapidly.features.common.ui.theme.RapidlyTheme

@Composable
fun LoadingButton(
    modifier: Modifier,
    isLoading: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !isLoading,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    //color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
@Preview
fun LoadingButtonPreview() {
    RapidlyTheme {
        LoadingButton(
            modifier = Modifier.fillMaxWidth(),
            isLoading = false,
            text = "Login",
            onClick = {}
        )
    }
}

@Composable
fun LoadingImageButton(
    modifier: Modifier,
    painter: Painter,
    isLoading: Boolean,
) {
    AnimatedVisibility(isLoading) {
        Box(
            modifier = modifier,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    AnimatedVisibility(!isLoading) {
        Image(
            modifier = modifier,
            painter = painter,
            contentDescription = null
        )
    }

}
