package ru.myitschool.work

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.*

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView
    private val apiService = Retrofit.Builder()
        .baseUrl("YOUR_API_BASE_URL") // Replace with your API base URL
        .build()
        .create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication) // Replace with your layout file

        usernameEditText = findViewById(R.id.username)
        loginButton = findViewById(R.id.login)
        errorTextView = findViewById(R.id.error)

        loginButton.isEnabled = false // Initially disabled
        usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateLoginButtonState()
            }
        })

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.authenticate(username)
                    if (response.isSuccessful) {
                        // Store credentials securely using KeyStore
                        // Navigate to the main activity
                    } else {
                        withContext(Dispatchers.Main) {
                            errorTextView.text = "Authentication failed"
                            errorTextView.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        errorTextView.text = "Network error"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateLoginButtonState() {
        val username = usernameEditText.text.toString()
        loginButton.isEnabled = username.length >= 3 && username.firstOrNull()?.isDigit() != true && username.matches(Regex("^[a-zA-Z0-9]+$"))
    }
}

