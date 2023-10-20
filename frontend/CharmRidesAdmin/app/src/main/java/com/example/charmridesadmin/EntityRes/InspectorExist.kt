package com.example.charmridesadmin.EntityRes

data class InspectorExist(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<RecordItem>
)
data class RecordItem(
    val id: String,
    val name: String,
    val email: String,
    val mobile: Int,
    val address: String
)