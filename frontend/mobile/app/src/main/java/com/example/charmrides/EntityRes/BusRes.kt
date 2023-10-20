package com.example.charmrides.EntityRes

import java.io.Serializable

data class BusRes(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<BusItem>
):Serializable
data class BusItem(
    val id: String,
    val collectionId: String,
    val collectionName: String,
    val created: String,
    val updated: String,
    val name: String,
    val vehicleNumber: String,
    val busType: String,
    val seat: Int,
    val price: Int,
    val driver: String,
    val fromLocation: String,
    val toLocation: String,
    val schedule: String
):Serializable