package com.example.authenticator.Activity

import android.Manifest
import android.R.attr.country
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
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
import com.example.authenticator.EntityRes.CityNameRes
import com.example.authenticator.EntityRes.TripItem
import com.example.authenticator.EntityRes.TripRes
import com.example.authenticator.EntityRes.UserExist
import com.example.authenticator.R
import com.example.authenticator.RetrofitService.PbService
import com.example.authenticator.RetrofitService.RetrofitService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
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
    private var userLocation: Location? = null


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
                        getUserLastTrip(user.items[0].email)
                    }else{
                        mediaPlayerFailed = MediaPlayer.create(this@MainActivity,R.raw.auth_failed)
                        mediaPlayerFailed.start()
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Response Error in getAuth",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserExist>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error in getAuth",Toast.LENGTH_SHORT).show()
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
                        if(userTrip.items[0].endCors.isEmpty() && userTrip.items[0].destination.isEmpty()){
                           //should update the trip for ended credentials
                           updateUserLastTrip(userTrip.items[0])
                       }else{
                            //should insert a new record for the trip for user
                           var locationObj: Location? = null
                           getUserLocation {
                               locationObj = it
                               if (it != null) {
                                   insertTrip(it,email)
                               }
                           }
                       }
                    }else{
                        Toast.makeText(this@MainActivity,"First Trip",Toast.LENGTH_SHORT).show()
                        //should insert a new record for the trip for user
                        getUserLocation {
                            if (it != null) {
                                insertTrip(it,email)
                            }
                        }
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Response Error in LastTrip",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TripRes>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error in LastTrip",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateUserLastTrip(userLastTrip: TripItem) {
        val pbService = PbService()
        val authentication: UserTrip = pbService.getRetrofit().create(UserTrip::class.java)

        var locationObj: Location? = null
        var destination: String? = null
        var distance: Int? = 0
        var fare: Double? = null

        getUserLocation { userLocation ->
            locationObj = userLocation
            if (userLocation != null) {
                getCiyName(userLocation) { cityName ->
                    destination = cityName.plus_code.compound_code

                    getTripFare(userLastTrip.startCors, "${userLocation.latitude},${userLocation.longitude}") { route ->
                        if (route != null) {
                            distance = route.routes[0].legs[0].distance.value.toInt()
                            var endPoint = route.routes[0].legs[0].end_address
                            var startPoint = route.routes[0].legs[0].start_address
                            fare = distance!!.toDouble() * 25.50
                            val call: Call<TripItem> = authentication.updateUserLastTrip(
                                userLastTrip.id,
                                TripItem(
                                    endPoint,
                                    distance!!,
                                    "${locationObj?.latitude},${locationObj?.longitude}" ?: "",
                                    fare ?: 0.0,
                                    "",
                                    startPoint,
                                    userLastTrip.startCors,
                                    userLastTrip.userMail
                                )
                            )

                            call.enqueue(object : Callback<TripItem> {
                                override fun onResponse(call: Call<TripItem>, response: Response<TripItem>) {
                                    val trip = response.body()
                                    println(trip)
                                    if (response.isSuccessful) {
                                        // Trip Ended popup all the details from the response body
                                        Toast.makeText(this@MainActivity,"Trip Ended",Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@MainActivity, "Response Error in updateTrip", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<TripItem>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "Server Error in updateTrip", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun insertTrip(userLocation:Location,userEmail: String){
        val pbService = PbService()
        val authentication : UserTrip = pbService.getRetrofit().create(UserTrip::class.java)
        var originCityName = ""
        getCiyName(userLocation) {
            originCityName = it.plus_code.compound_code

            println("pkooo meee city Name: $originCityName , ${userLocation.longitude}")
            val call: Call<TripItem> = authentication.insertTrip(
                TripItem(
                    "",
                    0,
                    "",
                    0.0,
                    "",
                    originCityName,
                    "${userLocation.latitude},${userLocation.longitude}",
                    userEmail
                )
            )

            call.enqueue(object : Callback<TripItem> {
                override fun onResponse(call: Call<TripItem>, response: Response<TripItem>) {
                    val trip = response.body()
                    println(trip)
                    if (response.isSuccessful) {
                        //trip has started
                        Toast.makeText(this@MainActivity, "Trip Started", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Response Error in insertTrip",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<TripItem>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Server Error in insertTrip",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun getCiyName(userLocation:Location,callback: (CityNameRes) -> Unit){
        val retrofitService = RetrofitService()
        val authentication : GmapsCalculator = retrofitService.getRetrofit().create(GmapsCalculator::class.java)

        val latLongString = "${userLocation.latitude},${userLocation.longitude}"
        val call: Call<CityNameRes> = authentication.getCityName(latLongString)
        call.enqueue(object : Callback<CityNameRes> {
            override fun onResponse(call: Call<CityNameRes>, response: Response<CityNameRes>) {
                if (response.isSuccessful){
                    val cityName = response.body()
                    println(cityName)
                    if(cityName !=null){

                        callback(cityName)
                    }
                }else{
                    Toast.makeText(this@MainActivity,"Invalid Credentials in getCityname",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CityNameRes>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failed getCityname",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getTripFare(origin:String,destination:String,callback: (Route?) -> Unit) {
        val retrofitService = RetrofitService()
        val authentication: GmapsCalculator = retrofitService.getRetrofit().create(GmapsCalculator::class.java)


        val call: Call<Route> = authentication.getRouteDetails(origin, destination)

        call.enqueue(object : Callback<Route> {
            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                if (response.isSuccessful) {
                    val route = response.body()
                    println(route)
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

    private fun getUserLocation(callback: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLocation = location

                        // Use latitude and longitude as needed
                        callback(userLocation)
                    }
                }
        } else {
            // Handle the case where permission is not granted
        }
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