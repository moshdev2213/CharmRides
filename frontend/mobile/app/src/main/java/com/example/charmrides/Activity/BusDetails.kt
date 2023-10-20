package com.example.charmrides.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.charmrides.EntityRes.BusItem
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R


class BusDetails : AppCompatActivity() {
    private lateinit var out:UserRecord
    private lateinit var bus:BusItem

    private lateinit var cvBackBtn:CardView
    private lateinit var cvBuyBtn:CardView
    private lateinit var tvTrainToDetail:TextView
    private lateinit var tvTrainFromDetail:TextView
    private lateinit var tvSeatCal:TextView
    private lateinit var spinnerSeatSelect:Spinner

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_details)

        cvBackBtn = findViewById(R.id.cvBackBtn)
        cvBuyBtn = findViewById(R.id.cvBuyBtn)
        tvTrainToDetail = findViewById(R.id.tvTrainToDetail)
        tvTrainFromDetail = findViewById(R.id.tvTrainFromDetail)
        tvSeatCal = findViewById(R.id.tvSeatCal)
        spinnerSeatSelect = findViewById(R.id.spinnerSeatSelect)

        val bundle = intent.extras
        if (bundle != null) {
            // Retrieve the user and bus items
            out = bundle.getSerializable("user") as UserRecord
            bus = bundle.getSerializable("bus") as BusItem
            println("keriiiiiiiiioooooooooo "+bus.busType)
            // populate views
            tvTrainToDetail.text = bus.toLocation.capitalize()
            tvTrainFromDetail.text = bus.fromLocation.capitalize()
            tvSeatCal.text = "${bus.seat} Seats"
        }
        // Get the selected item from spinnerSeatSelect
        val selectedSeat = spinnerSeatSelect.selectedItem.toString()


        cvBackBtn.setOnClickListener {
            finish()
        }
        cvBuyBtn.setOnClickListener {
            val intent = Intent(this@BusDetails, Checkout::class.java)
            val bundle = Bundle()
            bundle.putSerializable("user", out)
            bundle.putSerializable("bus", bus)
            bundle.putString("seat", selectedSeat)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }
}