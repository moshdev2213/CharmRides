package com.example.authenticator.ApiRes

data class Route(
    val routes: List<RouteInfo>
)
data class RouteInfo(
    val legs: List<Leg>
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val end_address: String,
    val start_address: String
)

data class Distance(
    val text: String,
    val value: String
)

data class Duration(
    val text: String,
    val value: String
)
