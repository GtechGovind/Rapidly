package com.gtech.rapidly.features.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers

@Composable
fun ImageFromUrl(
    modifier: Modifier,
    imageUrl: String
) {

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center),
            model = imageRequest,
            contentDescription = imageUrl
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent(painter = painter)
                is AsyncImagePainter.State.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Failed to load image!",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ImageFromUrlPreview() {
    ImageFromUrl(
        modifier = Modifier.fillMaxSize(),
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/parcelpro-c38cd.appspot.com/o/nazz_hotel.png?alt=media&token=1d001373-97b8-425d-b176-c7d4df19b590"
    )
}
