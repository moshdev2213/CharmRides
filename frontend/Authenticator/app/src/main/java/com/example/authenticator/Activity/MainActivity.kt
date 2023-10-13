package com.example.authenticator.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.example.authenticator.ApiRes.Route
import com.example.authenticator.ApiServices.GmapsCalculator
import com.example.authenticator.R
import com.example.authenticator.RetrofitService.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var scannerQrFrame:CodeScannerView
    private lateinit var scannerQr:CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scannerQrFrame = findViewById(R.id.scannerQrFrame)

        scannerQr = CodeScanner(this,scannerQrFrame)
        getDirection()
    }
    private fun getDirection(){
        val retrofitService = RetrofitService()
        val authentication : GmapsCalculator = retrofitService.getRetrofit().create(GmapsCalculator::class.java)

        val origin = "7.4817644,80.36089819999999"
        val destination = "6.927044,79.86123599999999"

        val call: Call<Route> = authentication.getRouteDetails(origin,destination)
        call.enqueue(object : Callback<Route> {
            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                if (response.isSuccessful){
                    val patient = response.body()
                    if(patient !=null){
                       println("pkooo meee : "+ patient.routes[0].legs[0].end_address )
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Route>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failed",Toast.LENGTH_SHORT).show()
            }
        })
    }
}