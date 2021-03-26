package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.ToDoDatabase
import com.example.todoapp.data.getDataBase
import com.example.todoapp.data.repository.ToDoRepository

class ToDoApplication : Application(){
    val database: ToDoDatabase by lazy {
        getDataBase(this)
    }
    val repository: ToDoRepository by lazy {
        ToDoRepository(database.getToDoDao)
    }
}