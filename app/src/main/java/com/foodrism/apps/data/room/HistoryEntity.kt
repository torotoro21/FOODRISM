package com.foodrism.apps.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_history")
class HistoryEntity(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}