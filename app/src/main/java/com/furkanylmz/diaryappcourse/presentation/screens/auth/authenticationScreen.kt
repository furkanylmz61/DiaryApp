package com.furkanylmz.diaryappcourse.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun authenticationScreen(
    loadingState: Boolean,
    onButtonClicked: () -> Unit
) {
    Scaffold(
        content = {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClicked= onButtonClicked
            )
        }
    )
}