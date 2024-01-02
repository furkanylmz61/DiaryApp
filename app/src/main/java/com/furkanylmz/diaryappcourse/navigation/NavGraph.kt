package com.furkanylmz.diaryappcourse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furkanylmz.diaryappcourse.presentation.screens.auth.authenticationScreen
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController){
    NavHost(
        startDestination = startDestination,
        navController = navController
    ){
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(){
    composable(route= Screen.Authentication.route){
        val oneTabState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        authenticationScreen(
            loadingState = oneTabState.opened,
            oneTabState= oneTabState,
            messageBarState= messageBarState,
            onButtonClicked =
         {
            oneTabState.open()
        }
        )
    }
}
fun NavGraphBuilder.homeRoute(){
    composable(route= Screen.Home.route){
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