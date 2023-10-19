package com.example.charmrides.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.charmrides.FormData.UserLoginForm
import com.example.charmrides.R
import com.example.charmrides.Validation.ValidationResult

class SignIn : AppCompatActivity() {
    private lateinit var tvredirectToSignUp:TextView
    private lateinit var cvSignInBtn:CardView
    private lateinit var etSignInPassword:EditText
    private lateinit var etSignInEmail:EditText
    private  var count:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tvredirectToSignUp = findViewById(R.id.tvredirectToSignUp)
        cvSignInBtn = findViewById(R.id.cvSignInBtn)
        etSignInPassword = findViewById(R.id.etSignInPassword)
        etSignInEmail = findViewById(R.id.etSignInEmail)


    }
    
    fun validateForm(email:String,password:String){

        val userLoginForm = UserLoginForm(
            email,
            password
        )
        val emailValidation =userLoginForm.validateEmail()
        val passwordValidation =userLoginForm.validatePassword()

        when(emailValidation){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etSignInEmail.error =emailValidation.errorMsg
            }
            is ValidationResult.Empty ->{
                etSignInEmail.error =emailValidation.errorMsg
            }
        }

        when(passwordValidation){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etSignInPassword.error =passwordValidation.errorMsg

            }
            is ValidationResult.Empty ->{
                etSignInPassword.error =passwordValidation.errorMsg

            }
        }
    }
}