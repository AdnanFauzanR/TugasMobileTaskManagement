package com.d121201078.taskmanagement.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import com.d121201078.taskmanagement.Data.Model
import com.d121201078.taskmanagement.Data.TaskDatabase
import com.d121201078.taskmanagement.Data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    val getAllTask: LiveData<List<Model>>

    private val repository: TaskRepository
    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        getAllTask = repository.getAllTask
    }

    fun insertTask(task: Model)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.insertTask(task)
        }
    }

    fun doneTask(uid: Long){
        viewModelScope.launch(Dispatchers.IO){
            repository.doneTask(uid)
        }
    }
    fun updateTask(task: Model) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask(uid: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(uid)
        }
    }


}