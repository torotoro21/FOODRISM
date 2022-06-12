package com.foodrism.apps.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM db_history ORDER BY id ASC")
    fun readHistory(): LiveData<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: HistoryEntity)
}