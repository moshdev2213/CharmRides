package com.example.charmridesadmin.EntityRes

data class ComplainFormRes(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<ComplainItem>
)
data class ComplainItem(
    val id: String,
    val inspectorEmail: String,
    val userEmail: String,
    val userName: String,
    val busName: String,
    val description: String
)