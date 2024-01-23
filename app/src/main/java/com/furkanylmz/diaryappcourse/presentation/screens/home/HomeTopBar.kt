package com.furkanylmz.diaryappcourse.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.furkanylmz.diaryappcourse.R


@Composable
fun HomeTopBar(onMenuClicked: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onMenuClicked() }) { // Burada düzeltme yapıldı
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Hamburger Menu Icon",
                    tint = darkColorScheme().onSurface
                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            IconButton(onClick = { onMenuClicked() }) { // Burada düzeltme yapıldı
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon",
                )
            }
        }
    )
}
