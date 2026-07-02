package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDao
import com.example.data.PaymentMethod
import com.example.data.Tournament
import com.example.data.TournamentEntry
import com.example.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dao: AppDao) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    val tournaments = dao.getAllTournaments().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val currentPaymentMethod = dao.getPaymentMethod().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun loginOrRegister(username: String, isLogin: Boolean) {
        viewModelScope.launch {
            val user = dao.getUserByUsername(username)
            if (user != null) {
                _currentUser.value = user
            } else {
                // If the username is one of the predefined admins, create as admin.
                val role = if (username in listOf("admin1", "admin2", "admin3", "admin4")) "admin" else "user"
                val newUser = User(username = username, passwordHash = "default", role = role)
                val id = dao.insertUser(newUser)
                _currentUser.value = newUser.copy(id = id.toInt())
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun addTournament(name: String, game: String, prizePool: String, entryFee: String, date: String, time: String) {
        viewModelScope.launch {
            dao.insertTournament(
                Tournament(
                    name = name,
                    game = game,
                    prizePool = prizePool,
                    entryFee = entryFee,
                    date = date,
                    time = time
                )
            )
        }
    }

    fun updatePaymentMethod(type: String, details: String) {
        viewModelScope.launch {
            dao.insertPaymentMethod(PaymentMethod(type = type, details = details))
        }
    }

    fun joinTournament(tournamentId: Int, userId: Int, username: String) {
        viewModelScope.launch {
            dao.insertTournamentEntry(
                TournamentEntry(
                    tournamentId = tournamentId,
                    userId = userId,
                    username = username,
                    paymentStatus = "Pending" // Require admin approval
                )
            )
        }
    }

    fun approveEntry(entry: TournamentEntry) {
        viewModelScope.launch {
            dao.updateTournamentEntry(entry.copy(paymentStatus = "Approved"))
        }
    }

    fun getEntries(tournamentId: Int): StateFlow<List<TournamentEntry>> {
        return dao.getEntriesForTournament(tournamentId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
}

class MainViewModelFactory(private val dao: AppDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
