package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.ToDoDao
import com.example.todoapp.data.models.ToDoModel

class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoModel>>  = toDoDao.allData

    suspend fun insert(toDoModel: ToDoModel) {
        toDoDao.insert(toDoModel)
    }
    suspend fun updateModel(toDoModel: ToDoModel) {
        toDoDao.updateModel(toDoModel)
    }
    suspend fun deleteModel(toDoModel: ToDoModel) {
        toDoDao.deleteModel(toDoModel)
    }
    suspend fun deleteAllModels() {
        toDoDao.deleteAllModels()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoModel>> {
        return toDoDao.searchDatabase(searchQuery)
    }
}