package com.buenobueno.todoapp.database

import android.content.Context
import androidx.room.Room.databaseBuilder
import java.util.function.Consumer

class TodoTaskService private constructor(context: Context){
    private val todoTaskDao: TodoTaskDao?

    init {
        val db = databaseBuilder(context.applicationContext, AppDatabase::class.java, "todo_db").build()
        todoTaskDao = db.todoTaskDao()
    }

    interface OperationCallBack {
        fun onOperationCompleted()

        fun onError(e: Exception?)
    }

    fun readAllTodoTasks(onResult: Consumer<List<TodoTask?>?>?) {
        Thread {
            try {
                val todoTasks = todoTaskDao!!.getAllTodoTasks()
                onResult?.accept(todoTasks)
            } catch (e: Exception) {
                // Handle error here
            }
        }.start()
    }

    fun insertTodoTask(todoTask: TodoTask, callBack: OperationCallBack?) {
        Thread {
            try {
                println("Is this going through?")
                todoTaskDao!!.insertTodoTask(todoTask)
                println("It is!")
                callBack?.onOperationCompleted()
            } catch (e: Exception) {
                callBack?.onError(e)
            }
        }.start()
    }

    fun updateTodoTask(todoTask: TodoTask, callBack: OperationCallBack?) {
        Thread {
            try {
                todoTaskDao!!.updateTodoTask(todoTask)
                callBack?.onOperationCompleted()
            } catch (e: Exception) {
                callBack?.onError(e)
            }
        }.start()
    }

    fun deleteTodoTask(todoTask: TodoTask, callBack: OperationCallBack?) {
        Thread {
            try {
                todoTaskDao!!.deleteTodoTask(todoTask)
                callBack?.onOperationCompleted()
            } catch (e: Exception) {
                callBack?.onError(e)
            }
        }.start()
    }

    companion object {
        private var instance: TodoTaskService? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): TodoTaskService? {
            if (instance == null) {
                instance = TodoTaskService(context)
            }
            return instance
        }
    }
}