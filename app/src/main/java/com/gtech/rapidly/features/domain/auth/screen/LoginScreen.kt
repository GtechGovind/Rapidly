package com.gtech.rapidly.features.domain.auth.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.domain.admin.screen.AdminDashboardScreen
import com.gtech.rapidly.features.domain.auth.viewmodel.LoginViewModel
import com.gtech.rapidly.features.domain.delivery.screen.DeliveryDashboardScreen
import com.gtech.rapidly.features.domain.terms.TermsAndConditionScreen

object LoginScreen: Screen {

    private fun readResolve(): Any = LoginScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            LoginViewModel(
                goToAdminDashboard = { navigator.replaceAll(AdminDashboardScreen) },
                goToDeliveryDashboard = { navigator.replaceAll(DeliveryDashboardScreen) }
            )
        }
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    private fun View(viewModel: LoginViewModel) {

        val navigator = LocalNavigator.currentOrThrow
        var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
        var password by remember { mutableStateOf(TextFieldValue()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(
                        text = "By proceeding, you agree to our Terms & Conditions. For assistance, contact the administrator. Click here for more information.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
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
                        modifier = Modifier,
                        text = "Continue",
                        isLoading = viewModel.isLoading,
                        onClick = {
                            viewModel.processLogin(
                                phoneNumber.text,
                                password.text
                            )
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    LoadingButton(
                        modifier = Modifier,
                        text = "New User? Register Now",
                        isLoading = viewModel.isLoading,
                        onClick = {
                            navigator.push(
                                RegisterScreen
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LoadingButton(
                        modifier = Modifier,
                        text = "Terms & Conditions",
                        isLoading = viewModel.isLoading,
                        onClick = {
                            navigator.push(
                                TermsAndConditionScreen
                            )
                        }
                    )
                }
            }
        }
    }
}