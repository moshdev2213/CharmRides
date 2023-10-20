package com.example.charmridesadmin.ApiService

import com.example.charmridesadmin.EntityRes.InspectorExist
import com.example.charmridesadmin.EntityRes.UserExist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthPb {
    @GET("/api/collections/users/records")
    fun getEmailExist(
        @Query("filter") filter:String,
        @Query("fields") field: String = "email"
    ): Call<UserExist>

    @GET("/api/collections/inspector/records")
    fun getInspectorExist(
        @Query("filter") filter:String
    ): Call<InspectorExist>
}