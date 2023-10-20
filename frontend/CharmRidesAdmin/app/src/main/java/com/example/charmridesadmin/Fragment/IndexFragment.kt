package com.example.charmridesadmin.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.charmridesadmin.Activity.ScanQr
import com.example.charmridesadmin.R


class IndexFragment : Fragment() {
    private lateinit var cvScanUserToken:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_index, container, false)

        cvScanUserToken = view.findViewById(R.id.cvScanUserToken)
        cvScanUserToken.setOnClickListener {
            startActivity(Intent(requireActivity(),ScanQr::class.java))
        }

        return  view
    }

}