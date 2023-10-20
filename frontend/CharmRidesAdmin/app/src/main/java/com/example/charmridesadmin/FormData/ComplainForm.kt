package com.example.charmridesadmin.FormData

import com.example.charmrides.Validation.ValidationResult

class ComplainForm(
    private var email:String,
    private var name:String,
    private var descrition:String
) {
    fun validateEmail(): ValidationResult {
        val regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}" // Regex to match email addresses

        return if(email.isEmpty()){
            ValidationResult.Empty("Please Enter Email")
        }else if(!email.matches(regex.toRegex())){
            ValidationResult.Invalid("Please Enter Valid Email")
        }else{
            ValidationResult.Valid
        }
    }
    fun validateDescription(): ValidationResult {

        return if(descrition.isEmpty()){
            ValidationResult.Empty("Please Enter Telephone")
        } else{
            ValidationResult.Valid
        }
    }
    fun validateName(): ValidationResult {
        // Regex to validate the name - allows letters, spaces, and may be more complex depending on your requirements
        val namePattern = "^[A-Za-z ]+$"

        return when {
            name.isEmpty() -> ValidationResult.Empty("Please Enter a Name")
            !name.matches(namePattern.toRegex()) -> ValidationResult.Invalid("Invalid Name")
            else -> ValidationResult.Valid
        }
    }

}