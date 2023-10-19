package com.example.charmrides.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class ScanQr : AppCompatActivity() {
    private lateinit var multiFormatWriter:MultiFormatWriter
    private lateinit var barcodeEncoder: BarcodeEncoder
    private lateinit var imgQrGenerator: ImageView
    private lateinit var out: UserRecord
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        //for the data incoming from the login
        val bundle = intent.extras
        out = bundle?.getSerializable("user", UserRecord::class.java)!!

        multiFormatWriter = MultiFormatWriter()
        barcodeEncoder = BarcodeEncoder()

        imgQrGenerator = findViewById(R.id.imgQrGenerator)
        val bitMatrix = multiFormatWriter.encode(out.record.email,BarcodeFormat.QR_CODE,300,300)
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
        imgQrGenerator.setImageBitmap(bitmap)
    }
}