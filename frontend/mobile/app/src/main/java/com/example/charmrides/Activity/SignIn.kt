package com.example.charmrides.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.charmrides.R

class SignIn : AppCompatActivity() {
    private lateinit var tvredirectToSignUp:TextView
    private lateinit var cvSignInBtn:CardView
    private lateinit var etSignInPassword:EditText
    private lateinit var etSignInEmail:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tvredirectToSignUp = findViewById(R.id.tvredirectToSignUp)
        cvSignInBtn = findViewById(R.id.cvSignInBtn)
        etSignInPassword = findViewById(R.id.etSignInPassword)
        etSignInEmail = findViewById(R.id.etSignInEmail)


    }
    fun validateForm(){

    }
}