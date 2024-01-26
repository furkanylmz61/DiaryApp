package com.furkanylmz.diaryappcourse.data.repository

import com.furkanylmz.diaryappcourse.model.Diary
import com.furkanylmz.diaryappcourse.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {

    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
}