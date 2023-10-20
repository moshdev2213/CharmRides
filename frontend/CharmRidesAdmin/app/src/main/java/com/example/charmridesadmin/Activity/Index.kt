package com.example.charmridesadmin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.charmridesadmin.Fragment.IndexFragment
import com.example.charmridesadmin.Fragment.ProfileFragment
import com.example.charmridesadmin.Fragment.ReportFragment
import com.example.charmridesadmin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Index : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        replaceFrag(IndexFragment())
        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.profile->replaceFrag(ProfileFragment())
                R.id.homie->replaceFrag(IndexFragment())
                R.id.report->replaceFrag(ReportFragment())
                else->{

                }
            }
            true
        }
    }
    private fun replaceFrag(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

//        if(userObj !=null){
//            val bundle = Bundle()
//            bundle.putSerializable("user",userObj)
//            fragment.arguments = bundle
//        }

        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}