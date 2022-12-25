package com.example.task1

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.task1.databinding.SignUpBinding

const val USER_PREFERENCES = "USER_PREFERENCES"

class AuthActivity : AppCompatActivity() {

    private lateinit var bind: SignUpBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        if (preferences.getString(USER_PREFERENCES, "") != "") {
            goNextActivity(preferences.getString(USER_PREFERENCES, "")!!)
        }
        destroyPreference()
        super.onCreate(savedInstanceState)
        bind = SignUpBinding.inflate(layoutInflater)
        setContentView(bind.root)
//        bind.cbRememberMe.seton { _, _ -> switchCheckBox() }
//        bind.cbRememberMe
        bind.btnRegister.setOnClickListener {registerNewUser()}
    }

    private fun registerNewUser() {
        val email = bind.etEmail.text.toString().lowercase()
        val password = bind.etPassword.text.toString()
        if (getValidityEmail(email) && getValidityPassword(password)) {
            val name = getName(email)
            if (bind.cbRememberMe.isChecked) {
                createPreference(name)
            }
            goNextActivity(name)
        } else {
            showError(getValidityEmail(email), getValidityPassword(password))
        }
    }

    private fun showError(validityEmail: Boolean, validityPassword: Boolean) {
        if (!validityEmail) {
            bind.etEmail.error = getString(R.string.invalid_email)
        }
        if (!validityPassword) {
            bind.etPassword.error = getString(R.string.invalid_password)
        }
    }

    private fun goNextActivity(name: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("name", name)
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun createPreference(name: String) {
        preferences.edit()
            .putString(USER_PREFERENCES, name)
            .apply()
    }

    private fun destroyPreference() {
        preferences.edit().clear().apply()
    }

    private fun getName(email: String): String {
        val partOfName = email.substring(0, email.indexOf('@'))
        return partOfName.replace("[-._]".toRegex(), " ")
    }

    private fun getValidityPassword(password: String): Boolean {
        /*
        https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-
        least-eight-characters-at-least-one-number-a

        Minimum eight characters, at least one uppercase letter,
        one lowercase letter and one number
         */
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
        println("${pattern.containsMatchIn(password)} pass")
        return pattern.containsMatchIn(password)
    }

    private fun getValidityEmail(email: String): Boolean {
        // regex for parsing valid email address from regexlib.com
        val pattern = Regex("([\\w.\\-_]+?@\\w+?\\x2E.+)")
        println("${pattern.containsMatchIn(email)} email")
        return pattern.containsMatchIn(email)
    }

    private fun switchCheckBox() {
        if (bind.cbRememberMe.isChecked) {
            bind.cbRememberMe.setButtonDrawable(R.drawable.ic_radio_button)
        } else {
            bind.cbRememberMe.setButtonDrawable(R.drawable.ic_radio_button_empty)
        }
    }
}