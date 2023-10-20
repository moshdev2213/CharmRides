package com.example.authenticator.Activity

import android.Manifest
import android.R.attr.country
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.authenticator.ApiRes.Route
import com.example.authenticator.ApiServices.AuthPb
import com.example.authenticator.ApiServices.GmapsCalculator
import com.example.authenticator.ApiServices.UserTrip
import com.example.authenticator.EntityRes.TripItem
import com.example.authenticator.EntityRes.TripRes
import com.example.authenticator.EntityRes.UserExist
import com.example.authenticator.R
import com.example.authenticator.RetrofitService.PbService
import com.example.authenticator.RetrofitService.RetrofitService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var scannerQrFrame:CodeScannerView
    private lateinit var scannerQr:CodeScanner
    private lateinit var cvProceedToExit:CardView
    private lateinit var mediaPlayerSuccess:MediaPlayer
    private lateinit var mediaPlayerFailed:MediaPlayer
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private var isProcessing = false
    private val handler = Handler()

    private val REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askPermissionForCamera()
        askPermissionForLocation()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        scannerQrFrame = findViewById(R.id.scannerQrFrame)
        cvProceedToExit = findViewById(R.id.cvProceedToExit)
        scannerQr = CodeScanner(this,scannerQrFrame)
        scannerQr.startPreview()

        scannerQr.decodeCallback = DecodeCallback { result ->
            runOnUiThread {

                getAuthEmail(result.text)
                if (!isProcessing) {
                    isProcessing = true
                    handler.postDelayed({
                        isProcessing = false
                        scannerQr.startPreview() // Restart the camera preview
                    }, 4000) // Adjust the delay in milliseconds (e.g., 2000ms = 2 seconds)
                }
            }
        }

        cvProceedToExit.setOnClickListener {
            finish()
        }

//        getDirection()
        getLastLocation()
        getTripFare { route ->
            if (route != null) {
                // Handle successful response
                println("End address: ${route.routes[0].legs[0].end_address}")
            } else {
                // Handle failure or error
                Toast.makeText(this@MainActivity, "Failed to get directions", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                            val addresses: List<Address>? =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            println("Lattitude: " + addresses!![0].latitude)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }
    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>,
        @NonNull grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please provide the required permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getAuthEmail(email:String){
        val pbService = PbService()
        val authentication : AuthPb = pbService.getRetrofit().create(AuthPb::class.java)

        val filterValue = "(email=\"$email\")"

        val call: Call<UserExist> = authentication.getEmailExist(filterValue)
        call.enqueue(object : Callback<UserExist> {
            override fun onResponse(call: Call<UserExist>, response: Response<UserExist>) {
                val user = response.body()
                if (response.isSuccessful){
                    if (user!!.items.isNotEmpty()) {
                        mediaPlayerSuccess = MediaPlayer.create(this@MainActivity,R.raw.auth_success)
                        mediaPlayerSuccess.start()
                    }else{
                        mediaPlayerFailed = MediaPlayer.create(this@MainActivity,R.raw.auth_failed)
                        mediaPlayerFailed.start()
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Response Error",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserExist>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserLastTrip(email: String){
        val pbService = PbService()
        val authentication : UserTrip = pbService.getRetrofit().create(UserTrip::class.java)

        val filterValue = "(userMail=\"$email\")"

        val call: Call<TripRes> = authentication.getUserLastTrip(filterValue)
        call.enqueue(object : Callback<TripRes> {
            override fun onResponse(call: Call<TripRes>, response: Response<TripRes>) {
                val userTrip = response.body()
                if (response.isSuccessful){
                    if (userTrip!!.items.isNotEmpty()) {
                       if(userTrip.items[0].endCors=="" && userTrip.items[0].destination==""){
                           //should update the trip for ended credentials
                       }else{
                           //should insert a new record for the trip for user
                       }
                    }else{
                        Toast.makeText(this@MainActivity,"No Trips",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Response Error",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TripRes>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserLastTrip(userLastTrip: TripItem){
        val pbService = PbService()
        val authentication : UserTrip = pbService.getRetrofit().create(UserTrip::class.java)

        val destination = getDestination()

        val call: Call<TripItem> = authentication.updateUserLastTrip(
            userLastTrip.id,
            TripItem(
                "colombo",
                103,
                "7.4817644,80.36089819999999",
                11110,
                "",
                "kurunegala",
                "7.4817644,80.36089819999999",
                userLastTrip.userMail
            )
        )

        call.enqueue(object : Callback<TripItem> {
            override fun onResponse(call: Call<TripItem>, response: Response<TripItem>) {
                val trip = response.body()
                if (response.isSuccessful){
                    //trip Ended popup all the details from the response body
                }else{
                    Toast.makeText(this@MainActivity,"Response Error",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TripItem>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun insertTrip(userLastTrip: TripItem){
        val pbService = PbService()
        val authentication : UserTrip = pbService.getRetrofit().create(UserTrip::class.java)

        val destination = getDestination()

        val call: Call<TripItem> = authentication.insertTrip(
            TripItem(
                "",
                0,
                "",
                0,
                "",
                "kurunegala",
                "7.4817644,80.36089819999999",
                userLastTrip.userMail
            )
        )

        call.enqueue(object : Callback<TripItem> {
            override fun onResponse(call: Call<TripItem>, response: Response<TripItem>) {
                val trip = response.body()
                if (response.isSuccessful){
                    //trip has started
                }else{
                    Toast.makeText(this@MainActivity,"Response Error",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TripItem>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDestination(){

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
    private fun getTripFare(callback: (Route?) -> Unit) {
        val retrofitService = RetrofitService()
        val authentication: GmapsCalculator = retrofitService.getRetrofit().create(GmapsCalculator::class.java)

        val origin = "7.4817644,80.36089819999999"
        val destination = "6.927044,79.86123599999999"

        val call: Call<Route> = authentication.getRouteDetails(origin, destination)

        call.enqueue(object : Callback<Route> {
            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                if (response.isSuccessful) {
                    val route = response.body()
                    callback(route) // Pass the result to the callback
                } else {
                    callback(null) // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<Route>, t: Throwable) {
                callback(null) // Handle network or other exceptions
            }
        })
    }


    private fun askPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 101)
            }
        }
    }
    private fun askPermissionForLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 102)
            }
        }
    }

}