package com.example.authenticator.ApiServices

import com.example.authenticator.EntityRes.TripItem
import com.example.authenticator.EntityRes.TripRes
import com.example.authenticator.EntityRes.UserExist
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserTrip {
    @GET("/api/collections/users/records")
    fun getUserLastTrip(
        @Query("filter") filter:String,
        @Query("sort") sort:String = "-created" ,
        @Query("perPage") perPage: Int = 1
    ): Call<TripRes>

    @PATCH("/api/collections/trip/records/{id}")
    fun updateUserLastTrip(
        @Path("id") id:String,
        @Body tripItem:TripItem
    ):Call<TripItem>

    @POST("/api/collections/trip/records")
    fun insertTrip(
        @Body tripItem:TripItem
    ):Call<TripItem>
}