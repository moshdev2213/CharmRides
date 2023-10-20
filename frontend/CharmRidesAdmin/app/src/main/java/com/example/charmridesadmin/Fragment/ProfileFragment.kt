package com.example.charmridesadmin.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.charmridesadmin.Activity.SignIn
import com.example.charmridesadmin.R

class ProfileFragment : Fragment() {
    private lateinit var cvLogoutUser:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        cvLogoutUser = view.findViewById(R.id.cvLogoutUser)
        cvLogoutUser.setOnClickListener {
            startActivity(Intent(requireContext(),SignIn::class.java))
            requireActivity().finish()
        }
        return view
    }

}