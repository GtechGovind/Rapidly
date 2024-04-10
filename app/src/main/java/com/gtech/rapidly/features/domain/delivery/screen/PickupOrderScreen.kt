package com.gtech.rapidly.features.domain.delivery.screen

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.gtech.rapidly.R
import com.gtech.rapidly.features.activity.MainActivity
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.delivery.service.ImageToTextService
import com.gtech.rapidly.features.domain.delivery.viewmodel.PickupOrderViewModel

object PickupOrderScreen : Screen {

    private fun readResolve(): Any = PickupOrderScreen

    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            PickupOrderViewModel(
                onBack = {
                    navigator.pop()
                }
            )
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: PickupOrderViewModel
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            OrderScannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 16.dp),
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Please scan the receipt to pick up the order. We will verify the order details, and any misleading information can impact your rating and may lead to penalties.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                shape = RectangleShape
            ) {

                OrderDetails(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel = viewModel
                )

                LoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isLoading = viewModel.isLoading,
                    text = "Pickup Order",
                    onClick = {
                        viewModel.processOrder()
                    }
                )
            }

        }

    }

    @Composable
    private fun OrderScannerView(
        modifier: Modifier,
        viewModel: PickupOrderViewModel
    ) {

        val scannerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageResult =
                        GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                    if (imageResult != null) {
                        imageResult.pages?.firstOrNull()?.let {
                            viewModel.scannedOrder = it.imageUri
                            viewModel.processImage(it.imageUri)
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    MainActivity.instance,
                    e.message ?: "Failed to process image",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        fun startScanner() {
            ImageToTextService.scanner.getStartScanIntent(MainActivity.instance)
                .addOnSuccessListener {
                    scannerLauncher.launch(
                        IntentSenderRequest.Builder(it).build()
                    )
                }
                .addOnFailureListener {
                    Toast.makeText(
                        MainActivity.instance,
                        "Failed to start scanner",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        ElevatedCard(
            modifier = modifier,
            onClick = { startScanner() },
            enabled = !viewModel.isLoading
        ) {

            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale *= zoomChange
                offset += offsetChange
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                if (viewModel.scannedOrder == Uri.EMPTY) {
                    Image(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .clickable { startScanner() },
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Scan Order"
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                            .transformable(state = state),
                        model = viewModel.scannedOrder,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                }

                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .clickable { startScanner() },
                    painter = painterResource(id = R.drawable.retry),
                    contentDescription = "Retry"
                )

            }

        }

    }

    @Composable
    private fun OrderDetails(
        modifier: Modifier,
        viewModel: PickupOrderViewModel
    ) {
        Column(
            modifier = modifier
        ) {

            SelectRestaurant(
                modifier = Modifier
                    .fillMaxWidth(),
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.billNumber,
                onValueChange = { viewModel.billNumber = it },
                enabled = !viewModel.isLoading,
                label = { Text("Bill Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Bill Number"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.amount,
                onValueChange = { viewModel.amount = it },
                enabled = !viewModel.isLoading,
                label = { Text("Order Amount") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Order Amount"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.customerNumber,
                onValueChange = { viewModel.customerNumber = it },
                enabled = !viewModel.isLoading,
                label = { Text("Customer Phone Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Customer Phone Number"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                )
            )

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SelectRestaurant(
        modifier: Modifier,
        viewModel: PickupOrderViewModel
    ) {

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = viewModel.selectedRestaurant,
                onValueChange = {},
                label = { Text("Select Restaurant") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                readOnly = true
            )

            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.restaurants.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = { Text(option.name) },
                        onClick = {
                            viewModel.selectedRestaurant = TextFieldValue(option.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        PickupOrderScreen.View(
            viewModel = PickupOrderViewModel(
                onBack = {

                }
            )
        )
    }
}
