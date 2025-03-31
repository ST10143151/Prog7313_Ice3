package com.fake.quizgame

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fake.quizgame.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateInput(name, email, password, confirmPassword)) {
                performRegistration(name, email, password)
            }
        }
    }

    private fun validateInput(
        name: String, email: String, password: String, confirmPassword: String
    ): Boolean {
        var isValid = true
        // Example check for the name field; add more validations as needed.
        if (name.isEmpty()) {
            binding.nameLayout.error = "Name is required"
            isValid = false
        } else {
            binding.nameLayout.error = null
        }
        // You should also validate email and password fields here.
        return isValid
    }

    private fun performRegistration(name: String, email: String, password: String) {
        showLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            showLoading(false)
                            if (profileTask.isSuccessful) {
                                // Create a centered toast message.
                                val toast = Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                toast.show()

                                // Navigate to the main page.
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                showError(profileTask.exception?.message ?: "Profile update failed")
                            }
                        }
                } else {
                    showLoading(false)
                    showError(task.exception?.message ?: "Registration failed")
                }
            }
    }

    private fun showLoading(show: Boolean) {
        binding.registerButton.isEnabled = !show
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
