package com.badhacker.tasko.ui.components

 
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.badhacker.tasko.R
import com.badhacker.tasko.utils.TaskFilter

@Composable
fun FilterTabs(
    selectedFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        selectedTabIndex = selectedFilter.ordinal,
        modifier = modifier.fillMaxWidth(),
        edgePadding = 16.dp
    ) {
        TaskFilter.values().forEach { filter ->
            Tab(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                text = {
                    Text(
                        text = when (filter) {
                            TaskFilter.ALL -> stringResource(R.string.all_tasks)
                            TaskFilter.WAIT_TO_START -> stringResource(R.string.wait_to_start)
                            TaskFilter.IN_PROGRESS -> stringResource(R.string.in_progres)
                            TaskFilter.FINISHED -> stringResource(R.string.finished)
                        }
                    )
                }
            )
        }
    }
}