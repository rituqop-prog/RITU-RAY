package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    // Tournaments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: Tournament): Long

    @Update
    suspend fun updateTournament(tournament: Tournament)

    @Query("SELECT * FROM tournaments ORDER BY id DESC")
    fun getAllTournaments(): Flow<List<Tournament>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    fun getTournamentById(id: Int): Flow<Tournament>

    // Payment Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethod)

    @Query("SELECT * FROM payment_methods ORDER BY id DESC LIMIT 1")
    fun getPaymentMethod(): Flow<PaymentMethod?>

    // Tournament Entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournamentEntry(entry: TournamentEntry)

    @Update
    suspend fun updateTournamentEntry(entry: TournamentEntry)

    @Query("SELECT * FROM tournament_entries WHERE tournamentId = :tournamentId")
    fun getEntriesForTournament(tournamentId: Int): Flow<List<TournamentEntry>>
    
    @Query("SELECT * FROM tournament_entries WHERE userId = :userId")
    fun getEntriesForUser(userId: Int): Flow<List<TournamentEntry>>
}
