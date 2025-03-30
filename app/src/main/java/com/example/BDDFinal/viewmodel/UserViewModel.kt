package com.example.BDDFinal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.BDDFinal.data.User
import com.example.BDDFinal.data.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun insertUser(user: User, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertUser(user)
            onResult(id)
        }
    }

    private fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword)
        return result.verified
    }

    fun login(login: String, password: String, onResult: (Boolean, User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByLogin(login.trim())
            if (user == null || !verifyPassword(password.trim(), user.password)) {
                onResult(false, null)
            } else {
                onResult(true, user)
            }
        }
    }
    fun registerUser(user: User, onResult: (Boolean, Long) -> Unit) {
        viewModelScope.launch {
            val result = repository.insertUserIfNotExists(user)
            if (result == -1L) {
                onResult(false, result)
            } else {
                onResult(true, result)
            }
        }
    }


}
