package com.example.todoapp.data.viewmodels

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(private val repository: ToDoRepository) : ViewModel() {
    val allData: LiveData<List<ToDoModel>> = repository.getAllData

    fun insertData(toDoModel: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(toDoModel)
        }
    }
    fun updateModel(toDoModel: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateModel(toDoModel)
        }
    }
    fun deleteModel(toDoModel: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteModel(toDoModel)
        }
    }
    fun deleteAllModel() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllModels()
        }
    }
}

class ToDoViewModelFactory(private val repository: ToDoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}