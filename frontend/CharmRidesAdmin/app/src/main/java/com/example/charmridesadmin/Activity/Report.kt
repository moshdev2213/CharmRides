package com.example.charmridesadmin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.charmrides.FormData.UserLoginForm
import com.example.charmrides.Validation.ValidationResult
import com.example.charmridesadmin.ApiService.AuthPb
import com.example.charmridesadmin.ApiService.ComplainService
import com.example.charmridesadmin.EntityRes.ComplainFormRes
import com.example.charmridesadmin.EntityRes.ComplainItem
import com.example.charmridesadmin.EntityRes.InspectorExist
import com.example.charmridesadmin.FormData.ComplainForm
import com.example.charmridesadmin.R
import com.example.charmridesadmin.RetrofitService.RetrofitService
import com.example.fitme.DialogAlerts.ProgressLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Report : AppCompatActivity() {
    private lateinit var etUserEmaiReport:EditText
    private lateinit var etEmailInspetor:EditText
    private lateinit var etNameUser:EditText
    private lateinit var etNameBus:EditText
    private lateinit var etDescriptionReport:EditText
    private lateinit var progressLoader: ProgressLoader
    private lateinit var cvCloseForm: CardView
    private lateinit var cvsubmitCoplainForm: CardView
    private  var count:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        etDescriptionReport = findViewById(R.id.etDescriptionReport)
        etNameBus = findViewById(R.id.etNameBus)
        etNameUser = findViewById(R.id.etNameUser)
        etEmailInspetor = findViewById(R.id.etEmailInspetor)
        etUserEmaiReport = findViewById(R.id.etUserEmaiReport)
        cvCloseForm = findViewById(R.id.cvCloseForm)
        cvsubmitCoplainForm = findViewById(R.id.cvsubmitCoplainForm)

        cvsubmitCoplainForm.setOnClickListener {
            insertReportForm(
                etEmailInspetor.text.toString(),
                etUserEmaiReport.text.toString(),
                etNameUser.text.toString(),
                etNameBus.text.toString(),
                etDescriptionReport.text.toString()
            )
        }
        cvCloseForm.setOnClickListener {
            finish()
        }
    }
    private fun insertReportForm(email:String,userEmail:String,userName:String,busName:String,descrpition:String){
        validateForm(email, userEmail,userName, busName, descrpition)
        if(count==5){
            progressLoader = ProgressLoader(
                this@Report,"Submitting Form","Please Wait"
            )
            progressLoader.startProgressLoader()
            val retrofitService = RetrofitService()
            val authService: ComplainService = retrofitService.getRetrofit().create(ComplainService::class.java)

            val call: Call<ComplainItem> = authService.createNewReport(
                ComplainItem(
                    "",
                    email,
                    userEmail,
                    userName,
                    busName,
                    descrpition
                )
            )
            call.enqueue(object : Callback<ComplainItem> {
                override fun onResponse(call: Call<ComplainItem>, response: Response<ComplainItem>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            val intent = Intent(this@Report, Index::class.java)
                            startActivity(intent)
                            progressLoader.dismissProgressLoader()
                            finish()
                        }
                    } else {
                        Toast.makeText(this@Report, "Invalid response", Toast.LENGTH_SHORT).show()
                        progressLoader.dismissProgressLoader()
                    }
                }
                override fun onFailure(call: Call<ComplainItem>, t: Throwable) {
                    Toast.makeText(this@Report, "Server Error", Toast.LENGTH_SHORT).show()
                    progressLoader.dismissProgressLoader()
                }
            })
            count=0;
        }
        count=0;
    }
    private fun validateForm(email:String,userEmail:String,name:String,busName:String,descrpition:String){

        val complainForm =ComplainForm(
            email,
            name,
            descrpition
        )
        val emailValidation =complainForm.validateEmail()
        val userEmailValidation =complainForm.validateEmail()
        val userName =complainForm.validateName()
        val busName =complainForm.validateName()
        val description =complainForm.validateDescription()

        when(emailValidation){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etEmailInspetor.error =emailValidation.errorMsg
            }
            is ValidationResult.Empty ->{
                etEmailInspetor.error =emailValidation.errorMsg
            }
        }

        when(userEmailValidation){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etUserEmaiReport.error =userEmailValidation.errorMsg

            }
            is ValidationResult.Empty ->{
                etUserEmaiReport.error =userEmailValidation.errorMsg

            }
        }
        when(userName){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etNameUser.error =userName.errorMsg

            }
            is ValidationResult.Empty ->{
                etNameUser.error =userName.errorMsg

            }
        }
        when(busName){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etNameBus.error =busName.errorMsg

            }
            is ValidationResult.Empty ->{
                etNameBus.error =busName.errorMsg

            }
        }
        when(description){
            is ValidationResult.Valid ->{ count ++ }
            is ValidationResult.Invalid ->{
                etDescriptionReport.error =description.errorMsg

            }
            is ValidationResult.Empty ->{
                etDescriptionReport.error =description.errorMsg

            }
        }
    }
}