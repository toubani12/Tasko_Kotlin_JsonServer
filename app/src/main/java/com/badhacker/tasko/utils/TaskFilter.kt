package com.badhacker.tasko.utils

import com.badhacker.tasko.data.model.TaskState

enum class TaskFilter(val matches: (TaskState) -> Boolean) {
    ALL({ true }),
    WAIT_TO_START({ it == TaskState.WAIT_TO_START }),
    IN_PROGRESS({ it == TaskState.IN_PROGRESS }),
    FINISHED({ it == TaskState.FINISHED })
}