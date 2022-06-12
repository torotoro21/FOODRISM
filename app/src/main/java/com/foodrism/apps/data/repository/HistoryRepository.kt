package com.foodrism.apps.data.repository

import androidx.lifecycle.LiveData
import com.foodrism.apps.data.room.HistoryDao
import com.foodrism.apps.data.room.HistoryEntity

class HistoryRepository(private val dao: HistoryDao) {
    val readHistory: LiveData<List<HistoryEntity>> = dao.readHistory()
    suspend fun insertHistory(history: HistoryEntity) {
        dao.insertHistory(history)
    }
}