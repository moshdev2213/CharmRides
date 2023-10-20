package com.example.charmridesadmin.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.charmridesadmin.ApiService.AuthPb
import com.example.charmridesadmin.EntityRes.UserExist
import com.example.charmridesadmin.R
import com.example.charmridesadmin.RetrofitService.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanQr : AppCompatActivity() {
    private lateinit var scannerQrFrame: CodeScannerView
    private lateinit var scannerQr: CodeScanner
    private lateinit var cvProceedInquiy: CardView
    private lateinit var mediaPlayerSuccess: MediaPlayer
    private lateinit var mediaPlayerFailed: MediaPlayer
    private var isProcessing = false
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        scannerQrFrame = findViewById(R.id.scannerQrFrame)
        cvProceedInquiy = findViewById(R.id.cvProceedInquiy)
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

        cvProceedInquiy.setOnClickListener {
            finish()
        }
    }
    private fun getAuthEmail(email:String){
        val pbService = RetrofitService()
        val authentication : AuthPb = pbService.getRetrofit().create(AuthPb::class.java)

        val filterValue = "(email=\"$email\")"

        val call: Call<UserExist> = authentication.getEmailExist(filterValue)
        call.enqueue(object : Callback<UserExist> {
            override fun onResponse(call: Call<UserExist>, response: Response<UserExist>) {
                val user = response.body()
                if (response.isSuccessful){
                    if (user!!.items.isNotEmpty()) {
                        mediaPlayerSuccess = MediaPlayer.create(this@ScanQr,R.raw.auth_success)
//                        mediaPlayerSuccess.start()
                        makeValidTokenPopup()
                    }else{
                        mediaPlayerFailed = MediaPlayer.create(this@ScanQr,R.raw.auth_failed)
//                        mediaPlayerFailed.start()
                        makeInValidTokenPopup()
                    }
                }else{
                    Toast.makeText(this@ScanQr,"Response Error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserExist>, t: Throwable) {
                Toast.makeText(this@ScanQr,"Server Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun makeValidTokenPopup(){
        val dialog = Dialog(this@ScanQr)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.valid_token)

        val btn = dialog.findViewById<Button>(R.id.dialogPopupValidBtn)
        btn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
    private fun makeInValidTokenPopup(){
        val dialog = Dialog(this@ScanQr)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.invalid_token)

        val btn = dialog.findViewById<Button>(R.id.dialogPopupInvalid)
        btn.setOnClickListener {
            startActivity(Intent(this@ScanQr,Report::class.java))
            finish()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}