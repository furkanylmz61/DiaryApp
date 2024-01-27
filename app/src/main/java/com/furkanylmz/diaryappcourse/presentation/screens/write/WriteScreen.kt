package com.furkanylmz.diaryappcourse.presentation.screens.write

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.furkanylmz.diaryappcourse.model.Diary
import com.furkanylmz.diaryappcourse.model.Mood


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteScreen(
    uiState: UiState,
    selectedDiary: Diary?,
    pagerState: PagerState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit) {
    LaunchedEffect(key1 = uiState.mood){
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold (
        topBar = {
                 WriteTopBar(
                     selectedDiary= selectedDiary,
                     onDeleteConfirmed = onDeleteConfirmed,
                     onBackPressed = onBackPressed,
                     )
        },
        content = {
            WriteContent(
                pagerState = pagerState,
                title = uiState.title,
                onTitleChanged =onTitleChanged,
                description =uiState.description ,
                onDescriptionChanged =onDescriptionChanged ,
                paddingValues = it
            )
        }
    )
}