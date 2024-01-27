package com.furkanylmz.diaryappcourse.presentation.screens.write

import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.furkanylmz.diaryappcourse.model.Mood
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
    }

    fun getDiaryIdArgument(){
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key= WRITE_SCREEN_ARGUMENT_KEY
            )
        )
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral
)