package com.example.authenticator.ApiServices

import com.example.authenticator.EntityRes.UserExist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthPb {
    @GET("/api/collections/users/records")
    fun getEmailExist(
        @Query("filter") filter:String,
        @Query("fields") field: String = "email"
    ): Call<UserExist>
}