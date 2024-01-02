package com.furkanylmz.diaryappcourse.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.furkanylmz.diaryappcourse.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun authenticationScreen(
    loadingState: Boolean,
    oneTabState : OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit
) {
    Scaffold(
        content = {
            ContentWithMessageBar(
                messageBarState = messageBarState
            ) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClicked= onButtonClicked
                    )
            }

        }
    )

    OneTapSignInWithGoogle(
        state = oneTabState,
        clientId =  CLIENT_ID ,
        onTokenIdReceived = {   tokenId ->
            Log.d("Auth", tokenId)
            messageBarState.addSuccess("Successfully Authenticated!")
        },
        onDialogDismissed ={
            message ->
            Log.d("Auth", message)
            messageBarState.addError(Exception(message))
        }
    )
}