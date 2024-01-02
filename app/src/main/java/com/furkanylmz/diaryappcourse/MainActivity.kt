package com.furkanylmz.diaryappcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.furkanylmz.diaryappcourse.navigation.Screen
import com.furkanylmz.diaryappcourse.navigation.SetupNavGraph
import com.furkanylmz.diaryappcourse.ui.theme.DiaryAppCourseTheme
import com.furkanylmz.diaryappcourse.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen()
            WindowCompat.setDecorFitsSystemWindows(window,false)
            DiaryAppCourseTheme {

                val navController= rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController =navController
                )

                }
            }
        }
    }
private fun getStartDestination(): String{
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route

}