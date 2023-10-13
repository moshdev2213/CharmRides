package com.example.authenticator.ApiServices

import com.budiyev.android.codescanner.BuildConfig
import com.example.authenticator.ApiRes.Route
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Properties

interface GmapsCalculator {
    @GET("maps/api/directions/json")
    fun getRouteDetails(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String = "AIzaSyCahfG4Sqf5mjnweqdMPqaillADQI0PPUM"
    ): Call<Route>
}