package com.badhacker.tasko.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.badhacker.tasko.data.model.Task
import com.badhacker.tasko.data.repository.TaskRepository
import com.badhacker.tasko.ui.components.FilterTabs
import com.badhacker.tasko.ui.components.TaskDisplay
import com.badhacker.tasko.ui.components.TaskInputField
import com.badhacker.tasko.ui.theme.TaskoTheme
import com.badhacker.tasko.ui.viewmodel.TaskViewModel
import com.badhacker.tasko.ui.viewmodel.TaskViewModelFactory
import com.badhacker.tasko.utils.TaskFilter

class MainActivity : ComponentActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        enableEdgeToEdge()
        // Create an instance of TaskRepository
        val repository = TaskRepository() // Ensure you have a way to create this

        // Use ViewModelProvider with the custom factory
        val viewModel: TaskViewModel = ViewModelProvider(this, TaskViewModelFactory(repository)).get(TaskViewModel::class.java)
        // Monitor network state
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("MainActivity", "Network available")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.e("MainActivity", "Network lost")
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Check initial network state
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_LONG).show()
        }

        setContent {
            TaskoTheme {
                // Use Surface to handle proper theming and edge-to-edge support
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    TaskApp(viewModel = viewModel)
                }
            }
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            FilterTabs(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
        },
        bottomBar = {
            TaskInputField(
                modifier = Modifier.padding(16.dp),
                onAddTask = { title -> viewModel.addTask(tasks.size + 1,title) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tasks.filter { selectedFilter.matches(it.state) },
                key = { it.id }
            ) { task ->
                TaskDisplay(
                    task = task,
                    onDelete = { viewModel.deleteTask(task.id) },
                    onEdit = { editingTask = task },
                    onStateChange = { newState ->
                        viewModel.updateTaskState(task.id, newState)
                    }
                )
            }
        }

        // Edit Task Dialog
        editingTask?.let { task ->
            EditTaskDialog(
                task = task,
                onDismiss = { editingTask = null },
                onConfirm = { updatedTitle ->
                    viewModel.updateTaskTitle(task.id, updatedTitle)
                    editingTask = null
                }
            )
        }
    }
}



@Composable
private fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var editedTitle by remember { mutableStateOf(task.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            TextField(
                value = editedTitle,
                onValueChange = { editedTitle = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(editedTitle.trim()) },
                enabled = editedTitle.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}