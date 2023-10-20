package com.example.charmridesadmin.Activity

import android.location.Location
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.charmridesadmin.R

class ScanQr : AppCompatActivity() {
    private lateinit var scannerQrFrame: CodeScannerView
    private lateinit var scannerQr: CodeScanner
    private lateinit var cvProceedToExit: CardView
    private lateinit var mediaPlayerSuccess: MediaPlayer
    private lateinit var mediaPlayerFailed: MediaPlayer
    private var isProcessing = false
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

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
                    Toast.makeText(this@MainActivity,"Response Error in getAuth", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserExist>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Server Error in getAuth", Toast.LENGTH_SHORT).show()
            }
        })
    }
}