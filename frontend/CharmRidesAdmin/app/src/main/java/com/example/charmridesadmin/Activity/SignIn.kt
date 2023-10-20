package com.example.charmridesadmin.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.charmrides.FormData.UserLoginForm
import com.example.charmrides.Validation.ValidationResult
import com.example.charmridesadmin.ApiService.AuthPb
import com.example.charmridesadmin.EntityRes.InspectorExist
import com.example.charmridesadmin.R
import com.example.charmridesadmin.RetrofitService.RetrofitService
import com.example.fitme.DialogAlerts.ProgressLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {
    private lateinit var cvSignInBtn: CardView
    private lateinit var etSignInPassword: EditText
    private lateinit var etSignInEmail: EditText
    private lateinit var progressLoader: ProgressLoader
    private  var count:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        cvSignInBtn = findViewById(R.id.cvSignInBtn)
        etSignInPassword = findViewById(R.id.etSignInPassword)
        etSignInEmail = findViewById(R.id.etSignInEmail)

        cvSignInBtn.setOnClickListener {
            authEmail(etSignInEmail.text.toString(),etSignInPassword.text.toString())
        }
        askPermissionForCamera()
        askPermissionForInternet()
    }

    private fun authEmail(email: String, password: String){
        validateForm(email,password)
        val filterValue = "(email='$email' && password='$password')"
        if(count==2){
            progressLoader = ProgressLoader(
                this@SignIn,"Verifying Login","Please Wait"
            )
            progressLoader.startProgressLoader()
            val retrofitService = RetrofitService()
            val authService: AuthPb = retrofitService.getRetrofit().create(AuthPb::class.java)

            val call: Call<InspectorExist> = authService.getInspectorExist(filterValue)
            call.enqueue(object : Callback<InspectorExist> {
                override fun onResponse(call: Call<InspectorExist>, response: Response<InspectorExist>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user!!.items.isNotEmpty()) {
                            startActivity(Intent(this@SignIn, Index::class.java))
                            progressLoader.dismissProgressLoader()
                            finish()
                        }else {
                            Toast.makeText(this@SignIn, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            progressLoader.dismissProgressLoader()
                        }
                    } else {
                        Toast.makeText(this@SignIn, "error", Toast.LENGTH_SHORT).show()
                        progressLoader.dismissProgressLoader()
                    }
                }

                override fun onFailure(call: Call<InspectorExist>, t: Throwable) {
                    Toast.makeText(this@SignIn, "Server Error", Toast.LENGTH_SHORT).show()
                    progressLoader.dismissProgressLoader()
                }
            })
            count=0;
        }
        count=0;

    }
    private fun validateForm(email:String,password:String){

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
    private fun askPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 101)
            }
        }
    }
    private fun askPermissionForInternet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), 102)
            }
        }
    }

}