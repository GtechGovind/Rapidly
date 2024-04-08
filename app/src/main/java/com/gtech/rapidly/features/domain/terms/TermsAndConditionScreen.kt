package com.gtech.rapidly.features.domain.terms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gtech.rapidly.features.common.firestore.model.Legal
import com.gtech.rapidly.features.common.ui.components.NavBar
import com.gtech.rapidly.features.common.ui.utils.SubscribeToLifecycle
import com.gtech.rapidly.features.common.ui.utils.WithTheme

object TermsAndConditionScreen : Screen {

    private fun readResolve(): Any = TermsAndConditionScreen
    private lateinit var navigator: Navigator
    
    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow
        val viewModel = viewModel<TermsAndConditionViewModel>()
        SubscribeToLifecycle(viewModel)
        View(viewModel)
    }

    @Composable
    private fun View(viewModel: TermsAndConditionViewModel) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            NavBar(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                title = "Terms and Conditions",
                onBack = { navigator.pop() }
            )

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        Modifier
                            .align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        viewModel.terms,
                        key = { it.id }
                    ) { term ->
                        TermVew(term)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TermVew(
        term: Legal
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = term.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = term.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


}

@Preview
@Composable
private fun Preview() {
    WithTheme {
        TermsAndConditionScreen.Content()
    }
}
