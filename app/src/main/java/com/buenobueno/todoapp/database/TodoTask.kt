package com.buenobueno.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoTask(
    @field:ColumnInfo(name = "title") var title: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}