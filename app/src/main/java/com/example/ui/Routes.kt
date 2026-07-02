package com.example.ui

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object AdminDashboardRoute

@Serializable
object UserDashboardRoute

@Serializable
data class TournamentDetailRoute(val tournamentId: Int)

@Serializable
object ManagePaymentRoute
