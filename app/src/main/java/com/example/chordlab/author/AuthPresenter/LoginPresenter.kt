package com.example.chordlab.auth.presenter

import com.example.chordlab.auth.model.AuthRepository
import com.example.chordlab.auth.model.User

class LoginPresenter(
    private var view: LoginContract.View?,
    private val authRepository: AuthRepository
) : LoginContract.Presenter {

    override fun login(email: String, password: String) {
        if (email.isBlank()) {
            view?.showEmailError("Email is required")
            return
        }

        if (password.isBlank()) {
            view?.showPasswordError("Password is required")
            return
        }

        if (password.length < 6) {
            view?.showPasswordError("Password must be at least 6 characters")
            return
        }

        val user = User(email, password)
        val isLoginSuccessful = authRepository.login(user)

        if (isLoginSuccessful) {
            view?.showLoginSuccess()
        } else {
            view?.showLoginError("Invalid email or password")
        }
    }

    override fun detachView() {
        view = null
    }
}