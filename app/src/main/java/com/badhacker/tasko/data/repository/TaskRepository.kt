package com.badhacker.tasko.data.repository

import com.badhacker.tasko.data.api.RetrofitClient
import com.badhacker.tasko.data.model.Task

class TaskRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getTasks() = apiService.getTasks()
    suspend fun addTask(id: Int, title: String) = apiService.createTask(Task(id, title))
    suspend fun updateTask(task: Task) = apiService.updateTask(task.id, task)
    suspend fun deleteTask(taskId: Int) = apiService.deleteTask(taskId)
}