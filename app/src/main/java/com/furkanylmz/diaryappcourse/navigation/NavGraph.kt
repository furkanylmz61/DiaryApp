package com.furkanylmz.diaryappcourse.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furkanylmz.diaryappcourse.data.repository.MongoDB
import com.furkanylmz.diaryappcourse.model.Mood
import com.furkanylmz.diaryappcourse.presentation.components.DisplayAlertDialog
import com.furkanylmz.diaryappcourse.presentation.screens.auth.AuthenticationViewModel
import com.furkanylmz.diaryappcourse.presentation.screens.auth.authenticationScreen
import com.furkanylmz.diaryappcourse.presentation.screens.home.HomeScreen
import com.furkanylmz.diaryappcourse.presentation.screens.home.HomeViewModel
import com.furkanylmz.diaryappcourse.presentation.screens.write.WriteScreen
import com.furkanylmz.diaryappcourse.presentation.screens.write.WriteViewModel
import com.furkanylmz.diaryappcourse.util.Constants.APP_ID
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.furkanylmz.diaryappcourse.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
){
    NavHost(
        startDestination = startDestination,
        navController = navController
    ){
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded =onDataLoaded
        )
        homeRoute(
            navigateToWrite = {
            navController.navigate(Screen.Write.route)
            },
            navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome : () -> Unit,
    onDataLoaded: () -> Unit
){
    composable(route= Screen.Authentication.route){
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTabState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()


        LaunchedEffect(key1 = Unit){
            onDataLoaded()
        }
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
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToAuth : () -> Unit,
    onDataLoaded: ()-> Unit
){
    composable(route= Screen.Home.route){
        val viewModel: HomeViewModel= viewModel()
        val diaries by viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        
        LaunchedEffect(key1 = diaries){
            if (diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }
        HomeScreen(
            diaries= diaries,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = { signOutDialogOpened = true },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs
        )
        LaunchedEffect(key1 = Unit){
            MongoDB.configureTheRealm()
        }
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


@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit){
    composable(
        route= Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){
        val viewModel: WriteViewModel= viewModel()
        val context = LocalContext.current
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState(pageCount = {Mood.values().size})
        val pageNumber by remember { derivedStateOf{pagerState.currentPage} }

        LaunchedEffect(key1 = uiState){
            Log.d("SelectedDiary", "${uiState.selectedDiaryId}")
        }

        WriteScreen (
            uiState= uiState,
            moodName = {Mood.values()[pageNumber].name },
            pagerState= pagerState,
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = {viewModel.setDescription(description = it)},
            onDeleteConfirmed= {viewModel.deleteDiary(
                onSuccess = {
                            Toast.makeText(
                                context,
                                "Deleted",
                                Toast.LENGTH_SHORT,
                            ).show()
                    onBackPressed()
                },
                onError = { message ->
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
                               },
            onDateTimeUpdated= {viewModel.updateDateTime(zonedDateTime = it)},
            onBackPressed = onBackPressed,
            onSaveClicked = {
                viewModel.upsertDiary(diary = it.apply { mood = Mood.values()[pageNumber].name},
                    onSuccess = { onBackPressed()},
                    onError = { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            )
    }
}