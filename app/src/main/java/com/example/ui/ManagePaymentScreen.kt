package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePaymentScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val currentPaymentMethod by viewModel.currentPaymentMethod.collectAsState()
    
    var type by remember { mutableStateOf(currentPaymentMethod?.type ?: "UPI") }
    var details by remember { mutableStateOf(currentPaymentMethod?.details ?: "") }

    LaunchedEffect(currentPaymentMethod) {
        if (currentPaymentMethod != null) {
            type = currentPaymentMethod!!.type
            details = currentPaymentMethod!!.details
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Payment Methods") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Set Payment Method for Entry Fees", style = MaterialTheme.typography.titleMedium)
            
            Row {
                RadioButton(selected = type == "UPI", onClick = { type = "UPI" })
                Text("UPI ID", modifier = Modifier.padding(start = 8.dp, top = 12.dp))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = type == "QR", onClick = { type = "QR" })
                Text("QR Text / Link", modifier = Modifier.padding(start = 8.dp, top = 12.dp))
            }
            
            OutlinedTextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Payment Details (e.g. 1234567890@upi)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = { viewModel.updatePaymentMethod(type, details) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Payment Method")
            }
        }
    }
}
