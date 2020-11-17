package com.misterjedu.jdformvalidator
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

data class JDataClass(
    val editText: EditText,
    val validator: (EditText) -> Boolean,
    val errorMessage: String,
    val editTextInputLayout: TextInputLayout?,
)