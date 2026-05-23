package com.example.chordlab.auth.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chordlab.R
import com.example.chordlab.auth.model.AuthRepository
import com.example.chordlab.auth.presenter.LoginContract
import com.example.chordlab.auth.presenter.LoginPresenter
import com.example.chordlab.dashboard.view.DashboardActivity
import com.example.chordlab.profile.UserSession

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        val authRepository = AuthRepository()
        presenter = LoginPresenter(this, authRepository)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            presenter.login(email, password)
        }
    }

    override fun showEmailError(message: String) {
        emailEditText.error = message
    }

    override fun showPasswordError(message: String) {
        passwordEditText.error = message
    }

    override fun showLoginSuccess() {
        UserSession.saveLogin(this, emailEditText.text.toString().trim())
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
