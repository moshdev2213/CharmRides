package com.example.charmrides.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.charmrides.Activity.Home
import com.example.charmrides.Activity.ScanQr
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R

class IndexFragment : Fragment() {
    private lateinit var cvBtnQrScanIndex:CardView
    private lateinit var out: UserRecord
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_index, container, false)

        out = arguments?.getSerializable("user", UserRecord::class.java)!!

        cvBtnQrScanIndex = view.findViewById(R.id.cvBtnQrScanIndex)
        cvBtnQrScanIndex.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("user", out)
            }
            val intent = Intent(requireActivity(), ScanQr::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        return  view
    }

}