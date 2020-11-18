package com.misterjedu.jdformvalidator

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

class JDFormValidator(builder: Builder) {

    private var fields: MutableList<JDataClass>
    private var viewsToBeVisible: MutableList<View>
    private var viewsToEnable: MutableList<View>
    private var shouldWatch: Boolean = false
    private var validatedIcon: Int? = null
    var areAllFieldsValidated: Boolean = false
    private var shouldRemoveErrorIcon: Boolean = false


    init {
        this.fields = builder.fields
        this.viewsToBeVisible = builder.fieldsToBeVisible
        this.viewsToEnable = builder.viewsToEnable
        this.shouldWatch = builder.shouldWatch
        this.validatedIcon = builder.validatedIcon
        this.shouldRemoveErrorIcon = builder.shouldRemoveErrorIcon
        startValidation()
    }


    private fun startValidation() {
        val globalFieldWatcher: TextWatcher = object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {

                //Validate all fields and return an array of the validation status of each field
                val editTextValidationArray = fieldsValidationLogic()

                //If there are views to be enabled or made visible, this is where to do that.
                if (shouldWatch && editTextValidationArray.contains(false)
                ) {
                    if (viewsToEnable.isNotEmpty()) {
                        for (view in viewsToEnable) {
                            view.isEnabled = false
                        }
                    }

                    if (viewsToBeVisible.isNotEmpty()) {
                        for (view in viewsToBeVisible) {
                            view.visibility = View.GONE
                        }
                    }


                } else if (editTextValidationArray.contains(true)) {

                    if (viewsToEnable.isNotEmpty()) {
                        for (view in viewsToEnable) {
                            view.isEnabled = true
                        }
                    }

                    if (viewsToBeVisible.isNotEmpty()) {
                        for (view in viewsToBeVisible) {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        //If EditTexts fields should be watched, add a watcher to all passed Edit Fields
        if (shouldWatch) {
            for (field in fields) {
                field.editText.addTextChangedListener(globalFieldWatcher)
            }
        } else {
            validateWithoutWatching()
        }

    }


    // Validation without text watcher.
    //All fields are validated when user clicks the button and sets the areAllFieldsValidated Boolean
    private fun validateWithoutWatching() {
        //Validate all fields and return an array of the validation status of each field
        val editTextValidationArray = fieldsValidationLogic()
        areAllFieldsValidated = !editTextValidationArray.contains(false)
    }


    private fun fieldsValidationLogic(): MutableList<Boolean> {

        val editTextValidationArray: MutableList<Boolean> = mutableListOf()

        /*If there are no fields to hide/show/enable/disable, the fields are watched
        * individually.
         */
        for (field in fields) {

            //If fields are validated and if there's a TIL or not, show error message
            if (field.validator.invoke(field.editText)) {

                //Remove all Errors when field is empty
                if (field.editText.text.toString().trim().isEmpty()) {
                    field.editTextInputLayout?.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    field.editTextInputLayout?.endIconDrawable = null
                    field.editTextInputLayout?.error = null
                    field.editText.error = null
                } else

                //Remove error icons if field is validated
                    if (field.editTextInputLayout != null) { //If there's a TextInputLayout
                        //Set Custom icon if it was set and validation passes
                        if (validatedIcon != null) {
                            field.editTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
                            field.editTextInputLayout.setEndIconDrawable(validatedIcon!!)
                        }

                        field.editTextInputLayout.error = null

                    } else { //If there's no TIL, use the editText
                        field.editText.error = null
                    }

                //Add true to the validation array
                editTextValidationArray.add(true)


            } else if (!field.validator.invoke(field.editText)) { //If validation fails, set Icon and Error Messages

                // Add Error Icons and Text if Edit Text fields are not empty
                if (field.editTextInputLayout != null &&
                        field.editText.text.toString().trim().isNotEmpty()
                ) {

                    //Remove Error Icon
                    if (shouldRemoveErrorIcon) {
                        field.editTextInputLayout.errorIconDrawable = null
                    }

                    //Remove Custom icon if it was set and validation fails
                    if (validatedIcon != null) {
                        field.editTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
                        field.editTextInputLayout.endIconDrawable = null
                    }

                    field.editTextInputLayout.error = field.errorMessage

                } else if (field.editTextInputLayout == null &&
                        field.editText.text.toString().trim().isNotEmpty()
                ) {
                    //Set Error on EditText if TextInputLayer is not available
                    field.editText.error = field.errorMessage
                }

                //Add false to the validation array
                editTextValidationArray.add(false)
            }
        }

        return editTextValidationArray
    }

    class Builder {

        internal var fields: MutableList<JDataClass> = mutableListOf()
            private set
        internal var fieldsToBeVisible: MutableList<View> = mutableListOf()
            private set
        internal var viewsToEnable: MutableList<View> = mutableListOf()
            private set
        internal var shouldWatch: Boolean = false
            private set
        internal var validatedIcon: Int? = null
            private set
        internal var shouldRemoveErrorIcon: Boolean = false
            private set

        fun addFieldsToValidate(fields: MutableList<JDataClass>): Builder {
            this.fields = fields
            return this
        }

        fun fieldsToShow(fieldsToShow: MutableList<View>): Builder {
            this.fieldsToBeVisible = fieldsToShow
            return this
        }

        fun viewsToEnable(viewsToEnable: MutableList<View>): Builder {
            this.viewsToEnable = viewsToEnable
            return this
        }

        fun watchWhileTyping(shouldWatch: Boolean): Builder {
            this.shouldWatch = shouldWatch
            return this
        }

        fun setValidatedIcon(iconResource: Int): Builder {
            this.validatedIcon = iconResource
            return this
        }

        fun removeErrorIcon(shouldRemoveErrorIcon: Boolean): Builder {
            this.shouldRemoveErrorIcon = shouldRemoveErrorIcon
            return this
        }


        fun build(): JDFormValidator {
            return JDFormValidator(this)
        }
    }
}
