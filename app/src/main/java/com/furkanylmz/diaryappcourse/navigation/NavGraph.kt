package com.furkanylmz.diaryappcourse.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furkanylmz.diaryappcourse.presentation.components.DisplayAlertDialog
import com.furkanylmz.diaryappcourse.presentation.screens.auth.AuthenticationViewModel
import com.furkanylmz.diaryappcourse.presentation.screens.auth.authenticationScreen
import com.furkanylmz.diaryappcourse.presentation.screens.home.HomeScreen
import com.furkanylmz.diaryappcourse.util.Constants.APP_ID
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController){
    NavHost(
        startDestination = startDestination,
        navController = navController
    ){
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        homeRoute(
            navigateToWrite = {
            navController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            }
        )
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome : () -> Unit
){
    composable(route= Screen.Authentication.route){
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTabState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        authenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTabState= oneTabState,
            messageBarState= messageBarState,
            onButtonClicked =
         {
            oneTabState.open()
             viewModel.setLoading(true)
        },
            onTokenIdRecived = {tokenId ->
             viewModel.signInWithMongoAtlas(
                 tokenId = tokenId,
                 onSuccess ={
                     messageBarState.addSuccess("Successfully Authenticated!")
                     viewModel.setLoading(false)
                 } ,onError= {
                     messageBarState.addError(it)
                     viewModel.setLoading(false)
                 }
             )
            },

            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message) )
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}
fun NavGraphBuilder.homeRoute(
    navigateToWrite : () -> Unit,
    navigateToAuth : () -> Unit
){
    composable(route= Screen.Home.route){
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        HomeScreen(
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = {
                               signOutDialogOpened = true
            },
            navigateToWrite =navigateToWrite
        )
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account",
            dialogOpened =signOutDialogOpened,
            onDialogClosed = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.Companion.create(APP_ID).currentUser
                    if (user != null){
                        user.logOut()
                        withContext(Dispatchers.Main){
                            navigateToAuth()
                        }
                    }
                }
            }
        )
    }
}


fun NavGraphBuilder.writeRoute(){
    composable(
        route= Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){

    }
}