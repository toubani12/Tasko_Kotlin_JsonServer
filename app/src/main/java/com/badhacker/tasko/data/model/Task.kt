package com.badhacker.tasko.data.model


enum class TaskState {
    WAIT_TO_START,
    IN_PROGRESS,
    FINISHED
}

data class Task(
    val id: Int,
    val title: String,
    val state: TaskState = TaskState.WAIT_TO_START
)