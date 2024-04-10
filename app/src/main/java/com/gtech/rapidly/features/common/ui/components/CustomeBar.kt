package com.gtech.rapidly.features.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gtech.rapidly.features.common.ui.utils.WithTheme

@Deprecated("Use DeliveryNavBar instead")
@Composable
fun NavBar(
    modifier: Modifier,
    title: String,
    onBack: (() -> Boolean)? = null,
    onProfile: (() -> Unit?)? = null
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = {
                        onBack()
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            } else {
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Back",
                    )
                }
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (onProfile != null) {
                IconButton(
                    onClick = {
                        onProfile()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Profile",
                    )
                }
            }
        }
    }
}

@Composable
fun AdminNavBar(
    modifier: Modifier,
    title: String,
    onLogOut: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = onLogOut,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Log Out",
                )
            }

        }
    }
}

@Composable
@Preview
fun NavBarPreview() {
    WithTheme {
        NavBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            title = "Title",
            onBack = {
                true
            },
            onProfile = {}
        )
    }
}

@Composable
@Preview
fun AdminNavBarPreview() {
    WithTheme {
        AdminNavBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            title = "Title",
            onLogOut = {}
        )
    }
}