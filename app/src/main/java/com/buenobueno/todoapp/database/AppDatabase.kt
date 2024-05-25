package com.buenobueno.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoTask::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoTaskDao(): TodoTaskDao?
}