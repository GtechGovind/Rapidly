package com.gtech.rapidly.features.domain.admin.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.ui.components.LoadingButton
import com.gtech.rapidly.features.common.ui.components.ModifyDeliveryBoyItem
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminDeliveryBoyListViewModel
import com.gtech.rapidly.features.domain.admin.viewmodel.AdminMainViewModel

object AdminDeliveryBoyListScreen : Screen {

    private fun readResolve(): Any = AdminDeliveryBoyListScreen
    private lateinit var navigator: Navigator

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            AdminDeliveryBoyListViewModel()
        }
        AdminMainViewModel.instance?.title = "Modify Users"
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    fun View(
        viewModel: AdminDeliveryBoyListViewModel
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            shape = MaterialTheme.shapes.medium,
            value = viewModel.query,
            onValueChange = {
                viewModel.query = it
                viewModel.filterUsers()
            },
            enabled = !viewModel.isLoading,
            label = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Search"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            )
        )

        UsersView(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            users = viewModel.users,
            viewModel = viewModel
        )

        if (viewModel.showPenaltyDialog) {
            PenaltyDialog(viewModel = viewModel)
        }

    }

    @Composable
    fun UsersView(
        modifier: Modifier,
        users: List<User> = emptyList(),
        viewModel: AdminDeliveryBoyListViewModel
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
        ) {
            items(
                users,
                key = { it.phoneNumber }
            ) { it ->
                ModifyDeliveryBoyItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    user = it,
                    isLoading = viewModel.isLoading,
                    onToggleUserStatus = { user ->
                        viewModel.toggleUserStatus(user)
                    },
                    onWithdraw = {

                    },
                    onPenalty = {
                        viewModel.showPenaltyDialog(it)
                    }
                )
            }
        }
    }

    @Composable
    private fun PenaltyDialog(
        viewModel: AdminDeliveryBoyListViewModel
    ) {
        Dialog(
            onDismissRequest = {
                viewModel.showPenaltyDialog = false
            },
        ) {
            ElevatedCard(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "ADD PENALTY",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        value = viewModel.penaltyAmount,
                        onValueChange = {
                            viewModel.penaltyAmount = it
                        },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        value = viewModel.penaltyReason,
                        onValueChange = {
                            viewModel.penaltyReason = it
                        },
                        label = { Text("Reason") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    LoadingButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        isLoading = viewModel.isLoading,
                        text = "Submit",
                    ) {
                        viewModel.applyPenalty()
                    }

                    LoadingButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        isLoading = viewModel.isLoading,
                        text = "Cancel",
                    ) {
                        viewModel.showPenaltyDialog = false
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
        AdminDeliveryBoyListScreen
            .View(AdminDeliveryBoyListViewModel().apply {
                users = listOf(
                    User("1")
                )
                showPenaltyDialog(users[0])
            })
    }
}