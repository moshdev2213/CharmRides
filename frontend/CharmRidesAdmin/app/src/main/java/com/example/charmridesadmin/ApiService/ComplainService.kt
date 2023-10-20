package com.example.charmridesadmin.ApiService

import com.example.charmridesadmin.EntityRes.ComplainFormRes
import com.example.charmridesadmin.EntityRes.ComplainItem
import com.example.charmridesadmin.FormData.ComplainForm
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ComplainService {
    @POST("/api/collections/complaint/records")
    fun createNewReport(
        @Body complainItem: ComplainItem
    ):Call<ComplainItem>

    @GET("/api/collections/complaint/records")
    fun getAllComplains(

    ):Call<ComplainFormRes>
}