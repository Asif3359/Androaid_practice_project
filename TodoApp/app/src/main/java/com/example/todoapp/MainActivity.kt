package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import java.text.SimpleDateFormat
import java.util.*

// Data class for Task
data class Task(
    val id: String,
    val title: String,
    val dateTime: String,
    val description: String,
    var isCompleted: Boolean = false,
    val reminderValue: String = "", // Reminder time
    val reminderUnit: String = ""   // Reminder unit (e.g., "Hours", "Days")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val taskList = remember { mutableStateListOf<Task>() }

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, taskList) }
        composable("addTask") { AddTaskScreen(navController, taskList) }
        composable("taskDetails/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val task = taskList.find { it.id == taskId }
            if (task != null) {
                TaskDetailsScreen(navController, task)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, taskList: MutableList<Task>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Todo List", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { /* Home functionality */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                },
                actions = {
                    IconButton(onClick = { /* More actions */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Actions")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addTask") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(taskList, key = { it.id }) { task ->
                TaskItem(task,
                    onToggleComplete = {
                        val index = taskList.indexOf(task)
                        taskList[index] = task.copy(isCompleted = !task.isCompleted)
                    },
                    onDelete = { taskList.remove(task) },
                    onClick = { navController.navigate("taskDetails/${task.id}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, taskList: MutableList<Task>) {
    var title by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var dateTime by remember { mutableStateOf("") }
    var reminderValue by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Hours") }
    var expanded by remember { mutableStateOf(false) } // Fix: Properly handle dropdown expansion
    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(navController.context, { _, year, month, day ->
        val timePicker = TimePickerDialog(navController.context, { _, hour, minute ->
            calendar.set(year, month, day, hour, minute)
            dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePicker.show()
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

    val reminderUnits = listOf("Hours", "Days", "Weeks", "Months")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Task", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item{
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(text = dateTime.ifBlank { "No date selected" }, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Button(onClick = { datePicker.show() }) { Text("Select Date & Time") }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Remind Me Before:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Reminder Value Input
                        OutlinedTextField(
                            value = reminderValue,
                            onValueChange = { reminderValue = it },
                            label = { Text("Enter Time") },
                            modifier = Modifier
                                .width(150.dp)
                                .height(60.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Dropdown for Reminder Units
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Unit") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .weight(1f)
                                    .height(60.dp),
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Unit")
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                reminderUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            selectedUnit = unit
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description for Reminder Input
                    Text(
                        text = "Set a reminder before the task time in hours, days, weeks, or months.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd // Ensures right alignment
                ) {
                    Button(
                        onClick = {
                            if (title.text.isNotBlank() && dateTime.isNotBlank()) {
                                taskList.add(
                                    Task(
                                        UUID.randomUUID().toString(),
                                        title.text,
                                        dateTime,
                                        description.text,
                                        reminderValue = reminderValue,
                                        reminderUnit = selectedUnit
                                    )
                                )
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Text("Add Task")
                    }
                }
            }

        }
    }
}

@Composable
fun TaskItem(task: Task, onToggleComplete: () -> Unit, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(task.dateTime, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                Checkbox(checked = task.isCompleted, onCheckedChange = { onToggleComplete() })
                IconButton(onClick = { onDelete() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(navController: NavController, task: Task) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Title
            Text("Title: ${task.title}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Date & Time
            Text("Date & Time: ${task.dateTime}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text("Description:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Reminder Section
            if (task.reminderValue.isNotBlank() && task.reminderUnit.isNotBlank()) {
                Text("Reminder: ${task.reminderValue} ${task.reminderUnit} before", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("Reminder: Not Set", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { task.isCompleted = true },
                modifier = Modifier.fillMaxWidth(0.45f)
            ) {
                    Text("Mark as Complete")
            }
        }
    }
}

@Preview
@Composable
fun PreviewTodoApp() {
    TodoApp()
}
