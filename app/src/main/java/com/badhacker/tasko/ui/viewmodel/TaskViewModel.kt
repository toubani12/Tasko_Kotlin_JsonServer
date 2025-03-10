package com.badhacker.tasko.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badhacker.tasko.data.model.Task
import com.badhacker.tasko.data.model.TaskState
import com.badhacker.tasko.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // Tag for logs
    companion object {
        private const val TAG = "TaskViewModel"
    }

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _tasks.value = repository.getTasks()
                Log.d(TAG, "Tasks loaded successfully: ${_tasks.value.size} items")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading tasks", e)
                _tasks.value = emptyList()
            }
        }
    }

    fun addTask(id: Int, title: String) {
        viewModelScope.launch {
            try {
                repository.addTask(id,title)
                Log.d(TAG, "Task added: $title")
                loadTasks()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding task: $title", e)
            }
        }
    }

    fun updateTaskState(taskId: Int, newState: TaskState) {
        viewModelScope.launch {
            try {
                _tasks.value.find { it.id == taskId }?.let { task ->
                    val updatedTask = task.copy(state = newState)
                    repository.updateTask(updatedTask)
                    Log.d(TAG, "Task state updated: ID $taskId -> $newState")
                    loadTasks()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating task state: ID $taskId", e)
            }
        }
    }

    fun updateTaskTitle(taskId: Int, newTitle: String) {
        viewModelScope.launch {
            try {
                _tasks.value.find { it.id == taskId }?.let { task ->
                    val updatedTask = task.copy(title = newTitle)
                    repository.updateTask(updatedTask)
                    Log.d(TAG, "Task title updated: ID $taskId -> $newTitle")
                    loadTasks()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating task title: ID $taskId", e)
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTask(taskId)
                Log.d(TAG, "Task deleted: ID $taskId")
                loadTasks()
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting task: ID $taskId", e)
            }
        }
    }
}
