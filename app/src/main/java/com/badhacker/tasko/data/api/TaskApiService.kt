package com.badhacker.tasko.data.api

import com.badhacker.tasko.data.model.Task
import retrofit2.Response
import retrofit2.http.*

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Task

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body task: Task): Task

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}