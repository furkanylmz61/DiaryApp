package com.furkanylmz.diaryappcourse.presentation.screens.home

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable


@Composable
fun HomeTopBar(onMenuClicked : () -> Unit){
    TopAppBar (
        navigationIcon = {
            IconButton(onClick = { onMenuClicked}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Hamburger Menu Icon",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            IconButton(onClick = onMenuClicked) {
                Icon(imageVector =Icons.Default.DateRange,
                    contentDescription ="Date Icon",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )

}

