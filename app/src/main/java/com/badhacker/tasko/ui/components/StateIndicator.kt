package com.badhacker.tasko.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badhacker.tasko.R
import com.badhacker.tasko.data.model.TaskState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateIndicator(
    currentState: TaskState,
    onStateChange: (TaskState) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { expanded = true }
                .background(getStateColor(currentState))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = getStateDisplayName(currentState),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.change_state),
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TaskState.values().forEach { state ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(getStateColor(state))
                            )

                            Text(text = getStateDisplayName(state))
                        }
                    },
                    onClick = {
                        onStateChange(state)
                        expanded = false
                    },
                    leadingIcon = if (state == currentState) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else null
                )
            }
        }
    }
}

private fun getStateColor(state: TaskState): Color {
    return when (state) {
        TaskState.WAIT_TO_START -> Color.Gray
        TaskState.IN_PROGRESS -> Color(0xFFFFA500) // Orange
        TaskState.FINISHED -> Color(0xFF4CAF50) // Green
    }
}

private fun getStateDisplayName(state: TaskState): String {
    return when (state) {
        TaskState.WAIT_TO_START -> "To Do"
        TaskState.IN_PROGRESS -> "In Progress"
        TaskState.FINISHED -> "Done"
    }
}

@Preview(showBackground = true)
@Composable
private fun StateIndicatorPreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TaskState.values().forEach { state ->
                StateIndicator(
                    currentState = state,
                    onStateChange = {}
                )
            }
        }
    }
}