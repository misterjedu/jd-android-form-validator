[![](https://jitpack.io/v/misterjedu/jd-android-form-validator.svg)](https://jitpack.io/#misterjedu/jd-android-form-validator)

# jd-android-form-validator
Simple and Flexible form validator for android

Validating input fields and forms in android is a a bit of an hassle, and most times, you'd have to write lots of codes every single time. I made a simple convenience class
for myself to handle every single validation I will ever need (at least for now), and now I'm making it a library. It's very easy to use, yet very powerful.

Once you set it up once, you'll find it easy and very convinient to use in most of your projects. You can check out the sample project (link below) to see a real implementation. 

# Usage

There are a few options you can set. 

- Fields you want to validate
- Fields to show (Visibility) when all fields are validated
- If you want to remove the default Icon
- The views you want to enable (isEnabled) when all the fields are validated
- If you want to remove the error icon
- A custom success icon when a field is validated (Optional)
- If you want to validate the fields while typing or not.

All you need is to set the options in the Validator Builder class as shown below

```sh
  JDFormValidator.Builder()
            .addFieldsToValidate( mutableListOf(JDataClass(), ...)
            .fieldsToShow(mutableListOf(view1, view2, ...))
            .removeErrorIcon(Boolean)
            .viewsToEnable(mutableListOf(view1, view2, ...))
            .setValidatedIcon(iconResource: Int)
            .watchWhileTyping(Boolean)
            .build()
```

Incase do not "watch while typing", you can know if all fields are validated on button click by calling the areAllFieldsValidated method.

```sh
 var validator =  JDFormValidator.Builder()
             .addFieldsToValidate(fields)
            .build()

	
	button.setOnClickListener{
		if(validator.areFiledsValidated){
			TODO( Do something)
		}else{
			TODO(Do nothing)
		}
	}
```



To set the fields to validate, all you need is to wrap each of the edit text, edit text input layout(optional), error message, and a validator lambda in a Jdatclass. 

```sh

      JDataClass(
                editText = binding.signInEditText,
                editTextInputLayout = binding.signInEditInputLayout,
                errorMessage = "Error, invalid email" ,
                validator = { it.jdValidateEmail(it.text.toString()) }
            )
```

You should create a JDataclass for each input field you wish to validate, and pass them all  as an array into the addFieldsToValidate method above.

PRO-TIP

You should have a class where you have EditText extension functions for your validators. Your validator should take in the parameter you want to validate and return a boolean. E.g, the validator below validates an email.

```sh
fun EditText.jdValidateEmail(email: String): Boolean {
    val pattern: Pattern =
        Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$");
    val matcher: Matcher = pattern.matcher(email);
    val matchFound = matcher.matches()

    return email.trim().isNotEmpty() && matchFound
}
```

I already provided a few EditText validator functions in the "JDValidatorFunctions" class, and you can create yours very easily. 

# Download

```sh
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# Gradle

```sh
	dependencies {
	        implementation 'com.github.misterjedu:jd-android-form-validator:Tag'
	}
  ```
# Example Project

Check out this sample project to see two different implementations of the form validator. 

https://github.com/misterjedu/jd-android-form-validator-example
  
  # License
  
  Copyright 2019 Oladokun Oladapo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


