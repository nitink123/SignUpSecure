package com.example.signupsecure

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.signupsecure.databinding.ActivitySignupBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity(),View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var mBinding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        // Set up spinner with country data (read from JSON file)
        val countryList = readCountryDataFromJSON(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinnerCountries.adapter = adapter
        mBinding.spinnerCountries.setSelection(0)
        mBinding.btnSignUp.setOnClickListener(this)

        mBinding.spinnerCountries.onItemSelectedListener = this




        // Set up text change listeners and initial button state
        setUpTextChangeListeners()
        updateSignUpButtonState()



    }

    private fun setUpTextChangeListeners() {
        mBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignUpButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        mBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignUpButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    // To validate the password For password, min 8 characters with atleast
   //one number, special characters[!@#$%&()], one lowercase letter, and one uppercase letter is
   //mandatory
    private fun validatePassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

    // To validate the name
    // For name, only alphabets are allowed
    private fun validateName(name: String): Boolean {
        val namePattern = "^[a-zA-Z]+$".toRegex()
        return namePattern.matches(name)
    }


    // To read data from json
    private fun readCountryDataFromJSON(context: Context): List<String> {
        val countryList = mutableListOf<String>()

        // Add a default value to the list
            countryList.add("PLEASE SELECT YOUR COUNTRY")
        try {
            // Read the JSON data from the assets directory
            val inputStream = context.assets.open("countryData.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            // Convert the buffer to a string
            val jsonData = String(buffer)

            // Parse the JSON data
            val jsonObject = JSONObject(jsonData)
            val dataObject = jsonObject.getJSONObject("data")

            // Iterate through the keys of the "data" object (e.g., "DZ", "AO", "BJ", ...)
            val keysIterator = dataObject.keys()
            while (keysIterator.hasNext()) {
                val countryCode = keysIterator.next()
                val countryObject = dataObject.getJSONObject(countryCode)
                val countryName = countryObject.getString("country")
                countryList.add(countryName)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return countryList
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSignUp -> {
                val name = mBinding.etName.text.toString()
                val password = mBinding.etPassword.text.toString()
                val selectedCountry = mBinding.spinnerCountries.selectedItem.toString()

                validateUser(name, password, selectedCountry)
            }

        }
    }

    // To validate the user
    private fun validateUser(name: String, password: String, selectedCountry: String) {
        if (validateName(name) && validatePassword(password)) {

            // Redirect to logout screen after successful sign-up
            startActivity(Intent(this, LogoutActivity::class.java))
            finish()
        } else {
            // Display appropriate error messages for invalid input
            Toast.makeText(this, "PLEASE PROVIDE APPROPRIATE PASSWORD", Toast.LENGTH_LONG).show()
            mBinding.etName.text = null
            mBinding.etPassword.text = null
            mBinding.spinnerCountries.setSelection(0)

        }
    }

    // once user select the country
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (position > 0) {
            // User selected a country, show the TextView
            mBinding.etName.visibility = View.VISIBLE
            mBinding.etPassword.visibility = View.VISIBLE
            mBinding.btnSignUp.visibility = View.VISIBLE
         //   mBinding.tvCountrySelected.text = parent?.getItemAtPosition(position).toString()
        } else {
            // User didn't select any country, hide the TextView
            mBinding.etName.visibility = View.GONE
            mBinding.etPassword.visibility = View.GONE
            mBinding.btnSignUp.visibility = View.GONE
        }
    }

    // if no country is selected
    override fun onNothingSelected(p0: AdapterView<*>?) {
        mBinding.etName.visibility = View.GONE
        mBinding.etPassword.visibility = View.GONE
        mBinding.btnSignUp.visibility = View.GONE
    }


    // To update the sign-up button state
    private fun updateSignUpButtonState() {
        val nameNotEmpty = mBinding.etName.text.toString().isNotEmpty()
        val passwordNotEmpty = mBinding.etPassword.text.toString().isNotEmpty()
        mBinding.btnSignUp.isEnabled = nameNotEmpty && passwordNotEmpty
    }


}