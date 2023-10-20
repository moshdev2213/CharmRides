package com.example.charmrides.EntityRes

data class PayRes (
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<PaymentItem>
    )
data class PaymentItem(
    val id: String,
    val busName: String,
    val userEmail: String,
    val seat: Int,
    val amount: Double
)
