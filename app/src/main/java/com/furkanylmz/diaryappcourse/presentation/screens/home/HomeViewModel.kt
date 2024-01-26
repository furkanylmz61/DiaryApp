package com.furkanylmz.diaryappcourse.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanylmz.diaryappcourse.data.repository.Diaries
import com.furkanylmz.diaryappcourse.data.repository.MongoDB
import com.furkanylmz.diaryappcourse.util.RequestState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)


    init {
        observeAllDiaries()
    }
    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }
}