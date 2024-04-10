package com.gtech.rapidly.features.domain.auth.screen

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.screen.AdminDashboardScreen
import com.gtech.rapidly.features.domain.auth.viewmodel.RegisterViewModel
import com.gtech.rapidly.features.domain.delivery.screen.DeliveryDashboardScreen
import com.gtech.rapidly.features.domain.delivery.screen.MainDeliveryScreen

object RegisterScreen : Screen {

    private fun readResolve(): Any = RegisterScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            RegisterViewModel(
                goToDeliveryDashboard = {
                    navigator.replaceAll(MainDeliveryScreen)
                },
                goToAdminDashboard = {
                    navigator.replaceAll(AdminDashboardScreen)
                }
            )
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    private fun View(viewModel: RegisterViewModel) {

        var fullName by remember { mutableStateOf(TextFieldValue()) }
        var addharNumber by remember { mutableStateOf(TextFieldValue()) }
        var drivingLicense by remember { mutableStateOf(TextFieldValue()) }
        var vehicleNumber by remember { mutableStateOf(TextFieldValue()) }
        var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
        var password by remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Register",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "By proceeding, you agree to our Terms & Conditions. For assistance, contact the administrator. Click here for more information.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fullName,
                onValueChange = { fullName = it },
                shape = MaterialTheme.shapes.medium,
                enabled = !viewModel.isLoading,
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name"
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
                value = addharNumber,
                onValueChange = { addharNumber = it },
                shape = MaterialTheme.shapes.medium,
                enabled = !viewModel.isLoading,
                label = { Text("Addhar Card Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Addhar Card Number"
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
                value = drivingLicense,
                onValueChange = { drivingLicense = it },
                shape = MaterialTheme.shapes.medium,
                enabled = !viewModel.isLoading,
                label = { Text("Driving Licence Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Driving Licence Number"
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
                value = vehicleNumber,
                onValueChange = { vehicleNumber = it },
                shape = MaterialTheme.shapes.medium,
                enabled = !viewModel.isLoading,
                label = { Text("Vehicle Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Vehicle Number"
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
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                shape = MaterialTheme.shapes.medium,
                enabled = !viewModel.isLoading,
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Number"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                shape = MaterialTheme.shapes.medium,
                onValueChange = { password = it },
                label = { Text("Password") },
                enabled = !viewModel.isLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue",
                isLoading = viewModel.isLoading,
                onClick = {
                    viewModel.processRegistration(
                        fullName.text,
                        addharNumber.text,
                        drivingLicense.text,
                        vehicleNumber.text,
                        phoneNumber.text,
                        password.text
                    )
                }
            )

        }

    }

}

@Composable
@Preview
private fun Preview() {
    WithTheme {
        RegisterScreen.Content()
    }
}
