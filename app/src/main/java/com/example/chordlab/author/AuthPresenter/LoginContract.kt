package com.example.chordlab.auth.presenter

interface LoginContract {

    interface View {
        fun showEmailError(message: String)
        fun showPasswordError(message: String)
        fun showLoginSuccess()
        fun showLoginError(message: String)
    }

    interface Presenter {
        fun login(email: String, password: String)
        fun detachView()
    }
}