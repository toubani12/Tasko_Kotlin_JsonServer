package com.badhacker.tasko.ui.components



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.badhacker.tasko.R

@Composable
fun TaskInputField(
    modifier: Modifier = Modifier,
    onAddTask: (String) -> Unit
) {
    var taskTitle by rememberSaveable { mutableStateOf("") }

    TextField(
        value = taskTitle,
        onValueChange = { taskTitle = it.uppercase() },
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.add_new_task)) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_task),
                modifier = Modifier.clickable {
                    if (taskTitle.isNotBlank()) {
                        onAddTask(taskTitle.trim())
                        taskTitle = ""
                    }
                }
            )
        },
        singleLine = true
    )
}