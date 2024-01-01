package com.furkanylmz.diaryappcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.furkanylmz.diaryappcourse.navigation.Screen
import com.furkanylmz.diaryappcourse.navigation.SetupNavGraph
import com.furkanylmz.diaryappcourse.ui.theme.DiaryAppCourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen()
            DiaryAppCourseTheme {

                val navController= rememberNavController()
                SetupNavGraph(
                    startDestination = Screen.Authentication.route,
                    navController =navController
                )

                }
            }
        }
    }