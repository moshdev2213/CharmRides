package com.example.authenticator.EntityRes

data class UserExist(
    val items: List<UserItem>,
    val page: Int,
    val perPage: Int,
    val totalItems: Int,
    val totalPages: Int
)
data class UserItem(
    val email: String
)
