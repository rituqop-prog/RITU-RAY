package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.Tournament

val gameModes = listOf("Battle Royale", "Clash Squad", "Lone Wolf", "Survival", "Free Match", "Sniper TDM")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    viewModel: MainViewModel,
    onNavigateToTournament: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val tournaments by viewModel.tournaments.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Welcome Back,", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(currentUser?.username ?: "Player", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AccountDetailsRow()
            }
            
            item {
                Text(
                    text = "eSport Games",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                GameModesGrid()
            }

            item {
                Text(
                    text = "Upcoming Tournaments",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(tournaments) { tournament ->
                TournamentCard(tournament = tournament, onClick = { onNavigateToTournament(tournament.id) })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun AccountDetailsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AccountDetailItem("My Profile", Icons.Default.AccountCircle)
        AccountDetailItem("My Wallet", Icons.Default.AccountCircle) // Replace with better icons later
        AccountDetailItem("Top Players", Icons.Default.AccountCircle)
        AccountDetailItem("Contact Us", Icons.Default.AccountCircle)
    }
}

@Composable
fun AccountDetailItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun GameModesGrid() {
    Column {
        for (i in gameModes.indices step 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (j in 0 until 3) {
                    if (i + j < gameModes.size) {
                        GameModeCard(gameModes[i + j], Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GameModeCard(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(0.8f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.DarkGray), // Placeholder for image
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(name, color = Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun TournamentCard(tournament: Tournament, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = tournament.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text(tournament.status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Game: ${tournament.game}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${tournament.date} | Time: ${tournament.time}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Entry: ${tournament.entryFee}", fontWeight = FontWeight.SemiBold)
                Text(text = "Prize: ${tournament.prizePool}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
