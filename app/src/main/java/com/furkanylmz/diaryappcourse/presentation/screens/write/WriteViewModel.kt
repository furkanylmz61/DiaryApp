package com.furkanylmz.diaryappcourse.presentation.screens.write

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanylmz.diaryappcourse.data.repository.MongoDB
import com.furkanylmz.diaryappcourse.model.Mood
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.furkanylmz.diaryappcourse.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    fun getDiaryIdArgument(){
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key= WRITE_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedDiary(){
        if (uiState.selectedDiaryId != null){
            viewModelScope.launch(Dispatchers.Main) {
                val diary =MongoDB.getSelectedDiary(
                    diaryId = ObjectId.invoke(uiState.selectedDiaryId!!)
                )
                if (diary is RequestState.Success) {
                    withContext(Dispatchers.Main) {
                        setTitle(title = diary.data.title)
                        setDescription(description = diary.data.description)
                        setMood(mood = Mood.valueOf(diary.data.mood))
                    }
                }
            }
        }
    }

    fun setTitle(title: String){
        uiState = uiState.copy(title = title)
    }
    fun setDescription(description: String){
        uiState = uiState.copy(description = description)
    }
    fun setMood(mood: Mood){
        uiState = uiState.copy(mood = mood)
    }
}



data class UiState(
    val selectedDiaryId: String? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral
)


