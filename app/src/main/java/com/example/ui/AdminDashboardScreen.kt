package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onNavigateToTournament: (Int) -> Unit,
    onNavigateToPaymentSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val tournaments by viewModel.tournaments.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToPaymentSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Payment Settings")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Tournament")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Manage Tournaments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tournaments) { tournament ->
                    TournamentCard(tournament = tournament, onClick = { onNavigateToTournament(tournament.id) })
                }
            }
        }
        
        if (showAddDialog) {
            AddTournamentDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, game, prizePool, entryFee, date, time ->
                    viewModel.addTournament(name, game, prizePool, entryFee, date, time)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddTournamentDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, game: String, prizePool: String, entryFee: String, date: String, time: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var game by remember { mutableStateOf("Free Fire") }
    var prizePool by remember { mutableStateOf("") }
    var entryFee by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Tournament") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tournament Name") }, singleLine = true)
                OutlinedTextField(value = game, onValueChange = { game = it }, label = { Text("Game") }, singleLine = true)
                OutlinedTextField(value = prizePool, onValueChange = { prizePool = it }, label = { Text("Prize Pool (e.g. ₹5000)") }, singleLine = true)
                OutlinedTextField(value = entryFee, onValueChange = { entryFee = it }, label = { Text("Entry Fee (e.g. ₹50)") }, singleLine = true)
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (DD/MM/YYYY)") }, singleLine = true)
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time (e.g. 5:00 PM)") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && prizePool.isNotBlank() && entryFee.isNotBlank()) {
                        onAdd(name, game, prizePool, entryFee, date, time)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
