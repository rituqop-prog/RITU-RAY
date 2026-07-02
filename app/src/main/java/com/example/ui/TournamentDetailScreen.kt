package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    tournamentId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val tournaments by viewModel.tournaments.collectAsState()
    val tournament = tournaments.find { it.id == tournamentId }
    val currentUser by viewModel.currentUser.collectAsState()
    val paymentMethod by viewModel.currentPaymentMethod.collectAsState()
    val entries by viewModel.getEntries(tournamentId).collectAsState()

    val isAdmin = currentUser?.role == "admin"
    val hasJoined = entries.any { it.userId == currentUser?.id }
    val userEntry = entries.find { it.userId == currentUser?.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tournament?.name ?: "Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
        }
    ) { padding ->
        if (tournament == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tournament not found")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(padding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.DarkGray)
                    ) {
                        Text("Tournament Banner Image Placeholder", modifier = Modifier.align(Alignment.Center), color = Color.White)
                    }

                    Text("${tournament.game.uppercase()} TOURNAMENT", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Time: ${tournament.date} at ${tournament.time}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailItem("PRIZE POOL", "🪙 ${tournament.prizePool}")
                        DetailItem("PER KILL", "🪙 10") // Placeholder
                        DetailItem("ENTRY FEE", "🪙 ${tournament.entryFee}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailItem("TYPE", "Squad")
                        DetailItem("VERSION", "TPP")
                        DetailItem("MAP", "Bermuda")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isAdmin) {
                        if (hasJoined) {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("You have joined this tournament!", fontWeight = FontWeight.Bold)
                                    Text("Payment Status: ${userEntry?.paymentStatus}")
                                }
                            }
                        } else {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Join Tournament", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (paymentMethod != null) {
                                        Text("Please pay ${tournament.entryFee} via ${paymentMethod!!.type}:")
                                        Text(paymentMethod!!.details, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = { viewModel.joinTournament(tournamentId, currentUser!!.id, currentUser!!.username) },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("I have paid & want to join")
                                        }
                                    } else {
                                        Text("Payment method not configured by admin.")
                                    }
                                }
                            }
                        }
                    } else {
                        Text("Participants & Payments", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(entries) { entry ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(entry.username, fontWeight = FontWeight.Bold)
                                            Text("Status: ${entry.paymentStatus}")
                                        }
                                        if (entry.paymentStatus == "Pending") {
                                            IconButton(onClick = { viewModel.approveEntry(entry) }) {
                                                Icon(Icons.Default.Check, contentDescription = "Approve", tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
