package com.example.charmrides.ApiService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/collections/users/auth-with-password")
    fun getUserAuth(
        @Body authPassEmail: AuthPassEmail
    ): Call<UserRecord>
}