package com.furkanylmz.diaryappcourse.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanylmz.diaryappcourse.data.repository.MongoDB
import com.furkanylmz.diaryappcourse.model.Diary
import com.furkanylmz.diaryappcourse.model.Mood
import com.furkanylmz.diaryappcourse.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.furkanylmz.diaryappcourse.util.RequestState
import com.furkanylmz.diaryappcourse.util.toRealmInstant
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime

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
                MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(uiState.selectedDiaryId!!))
                    .catch {
                        emit(RequestState.Error(Exception("Diary is already deleted")))
                    }
                    .collect {diary ->


                        if (diary is RequestState.Success) {
                            setSelectedDiary(diary = diary.data)
                            setTitle(title = diary.data.title)
                            setDescription(description = diary.data.description)
                            setMood(mood = Mood.valueOf(diary.data.mood))

                        }
                    }
            }
        }
    }

    private fun setSelectedDiary(diary: Diary){
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun setTitle(title: String){
        uiState = uiState.copy(title = title)
    }
    fun setDescription(description: String){
        uiState = uiState.copy(description = description)
    }
   private fun setMood(mood: Mood){
        uiState = uiState.copy(mood = mood)
    }

     fun updateDateTime(zonedDateTime: ZonedDateTime){
         uiState = uiState.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch (Dispatchers.IO){
            if (uiState.selectedDiaryId != null){
                updateDiary(diary= diary, onSuccess = onSuccess, onError= onError)
            }else{
                insertDiary(diary= diary, onSuccess= onSuccess, onError= onError)
            }
        }

    }

    private suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
            val result = MongoDB.insertDiary(diary = diary.apply {
                if (uiState.updatedDateTime != null){
                    date = uiState.updatedDateTime!!
                }
            })
            if (result is RequestState.Success){
                withContext(Dispatchers.Main){
                    onSuccess
                }
            }else if (result is RequestState.Error){
                withContext(Dispatchers.Main){
                    onError(result.error.message.toString())
                }
            }

    }

   private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        val result = MongoDB.updateDiary(diary= diary.apply {
            _id= ObjectId.invoke(uiState.selectedDiaryId!!)
            date = if (uiState.updatedDateTime !=null){
                uiState.updatedDateTime!!
            }else{
                uiState.selectedDiary!!.date
            }
        })
        if (result is RequestState.Success){
            withContext(Dispatchers.Main){
                onSuccess
            }
        }
        else if (result is RequestState.Error){
            withContext(Dispatchers.Main){
                onError(result.error.message.toString())
            }
        }

    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedDiaryId != null){
                val  result= MongoDB.deleteDiary(id = ObjectId.invoke(uiState.selectedDiaryId!!))
                if (result is RequestState.Success){
                    withContext(Dispatchers.Main){
                        onSuccess()
                    }
                }else if (result is RequestState.Error){
                    withContext(Dispatchers.Main){
                        onError(result.error.message.toString())
                    }
                }
            }
        }
    }
}



data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null
)


