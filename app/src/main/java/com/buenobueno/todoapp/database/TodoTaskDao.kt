package com.buenobueno.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface TodoTaskDao {

    @Query("SELECT * FROM TodoTask")
    fun getAllTodoTasks() : List<TodoTask?>?

    @Upsert
    fun insertTodoTask(todoTask: TodoTask)

    @Update
    fun updateTodoTask(todoTask: TodoTask)

    @Delete
    fun deleteTodoTask(todoTask: TodoTask)
}