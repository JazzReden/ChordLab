package com.example.chordlab.auth.model

class AuthRepository {

    fun login(user: User): Boolean {
        return user.email == "student@chordlab" &&
                user.password == "123456"
    }

    fun register(user: User): Boolean {
        return user.email.isNotBlank() && user.password.length >= 6
    }

    fun logout(): Boolean {
        return true
    }
}