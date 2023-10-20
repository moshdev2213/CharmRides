package com.example.charmrides.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.charmrides.ApiService.AuthService
import com.example.charmrides.ApiService.BusServices
import com.example.charmrides.Entity.AuthPassEmail
import com.example.charmrides.EntityRes.BusItem
import com.example.charmrides.EntityRes.PaymentItem
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R
import com.example.charmrides.RetrofitService.RetrofitService
import com.example.fitme.DialogAlerts.ProgressLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Checkout : AppCompatActivity() {

    private lateinit var out:UserRecord
    private lateinit var bus:BusItem
    private lateinit var seat:String

    private lateinit var tvTypeTrainCheck: TextView
    private lateinit var tvNameTrain: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvSubTotal: TextView
    private lateinit var cvProceedToPayAct: CardView
    private lateinit var btnBackReservation: ImageView
    private lateinit var progressLoader: ProgressLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        cvProceedToPayAct = findViewById(R.id.cvProceedToPayAct)
        tvSubTotal = findViewById(R.id.tvSubTotal)
        tvTotal = findViewById(R.id.tvTotal)
        tvNameTrain = findViewById(R.id.tvNameTrain)
        tvTypeTrainCheck = findViewById(R.id.tvTypeTrainCheck)
        btnBackReservation = findViewById(R.id.btnBackReservation)

        val bundle = intent.extras
        if (bundle != null) {
            out = bundle.getSerializable("user") as UserRecord
            seat = bundle.getString("seat").toString()
            bus = bundle.getSerializable("bus") as BusItem

            tvNameTrain.text = bus.name.capitalize()
            tvTypeTrainCheck.text = bus.busType
            tvTotal.text = "Rs ${bus.price}.00"
            tvSubTotal.text = "Rs ${bus.price}.00"
        }
        cvProceedToPayAct.setOnClickListener {
           proceedPay()
        }
    }
    private fun proceedPay(){
        progressLoader = ProgressLoader(
            this@Checkout,"Verifying Booking","Please Wait"
        )
        progressLoader.startProgressLoader()
        val retrofitService = RetrofitService()
        val authService: BusServices = retrofitService.getRetrofit().create(BusServices::class.java)

        val call: Call<PaymentItem> = authService.insertPay(
            PaymentItem(
                "",
                bus.name,
                out.record.email,
                seat.toInt(),
                bus.price.toDouble()
            )
        )
        call.enqueue(object : Callback<PaymentItem> {
            override fun onResponse(call: Call<PaymentItem>, response: Response<PaymentItem>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        val intent = Intent(this@Checkout, Home::class.java)
                        intent.putExtra("user", out) // Assuming "patient" is Parcelable or Serializable
                        startActivity(intent)
                        progressLoader.dismissProgressLoader()
                        finish()
                    }
                } else {
                    Toast.makeText(this@Checkout, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    progressLoader.dismissProgressLoader()
                }
            }

            override fun onFailure(call: Call<PaymentItem>, t: Throwable) {
                Toast.makeText(this@Checkout, "Server Error", Toast.LENGTH_SHORT).show()
                progressLoader.dismissProgressLoader()
            }
        })
    }
}