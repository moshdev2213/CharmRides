package com.example.authenticator.EntityRes

data class TripRes(
    val page: Int,
    val perPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<TripItem>
)

data class TripItem(
    val destination: String,
    val distance: Int,
    val endCors: String,
    val fare: Int,
    val id: String,
    val origin: String,
    val startCors: String,
    val userMail: String
)