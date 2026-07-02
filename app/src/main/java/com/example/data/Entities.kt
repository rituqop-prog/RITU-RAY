package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val role: String // "admin" or "user"
)

@Entity(tableName = "tournaments")
data class Tournament(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val game: String,
    val prizePool: String,
    val entryFee: String,
    val date: String,
    val time: String,
    val status: String = "Upcoming"
)

@Entity(tableName = "payment_methods")
data class PaymentMethod(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "UPI" or "QR"
    val details: String
)

@Entity(tableName = "tournament_entries")
data class TournamentEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tournamentId: Int,
    val userId: Int,
    val username: String,
    val paymentStatus: String // "Pending", "Approved", "Rejected"
)
