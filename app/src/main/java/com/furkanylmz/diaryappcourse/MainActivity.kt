package com.furkanylmz.diaryappcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.furkanylmz.diaryappcourse.ui.theme.DiaryAppCourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAppCourseTheme {

                }
            }
        }
    }