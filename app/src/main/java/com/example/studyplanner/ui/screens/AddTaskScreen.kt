package com.example.studyplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    var priority by remember { mutableStateOf("Normal") }
    var showPrioritySheet by remember { mutableStateOf(false) }

    var status by remember { mutableStateOf("Active") }
    var showStatusSheet by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                "Add Task",
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            TaskInputField("Name", title, { title = it })
            Spacer(modifier = Modifier.height(12.dp))

            TaskInputField("Description", description, { description = it })
            Spacer(modifier = Modifier.height(12.dp))

            TaskInputField("Subject", subject, { subject = it })
            Spacer(modifier = Modifier.height(12.dp))

            // DATE PICKER BUTTON
            SelectorButton(
                title = "Expiration date",
                value = selectedDate.ifEmpty { "Select date" },
                onClick = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TIME PICKER BUTTON
            SelectorButton(
                title = "Expiration time",
                value = selectedTime.ifEmpty { "Select time" },
                onClick = { showTimePicker = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // PRIORITY DROPDOWN (BottomSheet)
            SelectorButton(
                title = "Priority",
                value = priority,
                onClick = { showPrioritySheet = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // STATUS DROPDOWN (BottomSheet)
            SelectorButton(
                title = "Status",
                value = status,
                onClick = { showStatusSheet = true }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5123E8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", color = Color.White, fontSize = 18.sp)
            }
        }

        // DATE PICKER SHEET
        if (showDatePicker) {
            AppDatePicker(
                onDateSelected = {
                    selectedDate = it
                },
                onDismiss = { showDatePicker = false }
            )
        }

        // TIME PICKER SHEET
        if (showTimePicker) {
            AppTimePicker(
                onTimeSelected = {
                    selectedTime = it
                },
                onDismiss = { showTimePicker = false }
            )
        }

        // PRIORITY SHEET
        if (showPrioritySheet) {
            SelectionSheet(
                title = "Select priority",
                options = listOf("Low", "Normal", "High"),
                onSelect = {
                    priority = it
                    showPrioritySheet = false
                },
                onDismiss = { showPrioritySheet = false }
            )
        }

        // STATUS SHEET
        if (showStatusSheet) {
            SelectionSheet(
                title = "Select status",
                options = listOf("Active", "Completed", "Overdue"),
                onSelect = {
                    status = it
                    showStatusSheet = false
                },
                onDismiss = { showStatusSheet = false }
            )
        }
    }
}

@Composable
fun TaskInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun SelectorButton(title: String, value: String, onClick: () -> Unit) {
    Column {
        Text(title, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Color(0xFF5123E8), RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(value, color = Color.White, fontSize = 16.sp)
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {

    val datePickerState = rememberDatePickerState()

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(modifier = Modifier.padding(16.dp)) {

            DatePicker(state = datePickerState)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val formatted =
                            java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(java.util.Date(millis))

                        onDateSelected(formatted)
                    }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) {

    val timeState = rememberTimePickerState()

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TimePicker(state = timeState)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val time = String.format("%02d:%02d", timeState.hour, timeState.minute)
                    onTimeSelected(time)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionSheet(
    title: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(modifier = Modifier.padding(20.dp)) {

            Text(title, fontSize = 22.sp)

            Spacer(modifier = Modifier.height(20.dp))

            options.forEach { option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(option) }
                        .padding(vertical = 14.dp)
                ) {
                    Text(option, fontSize = 18.sp)
                }
            }
        }
    }
}
