package com.badhacker.tasko.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.badhacker.tasko.data.model.Task
import com.badhacker.tasko.data.model.TaskState

@Composable
fun TaskDisplay(
    task: Task,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onStateChange: (TaskState) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                StateIndicator(
                    currentState = task.state,
                    onStateChange = onStateChange
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit task",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onEdit),
                    tint = MaterialTheme.colorScheme.primary
                )

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onDelete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}