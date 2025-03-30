package com.example.BDDFinal.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun insertUser(user: User): Long = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun getUserByLogin(login: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByLogin(login)
    }

    suspend fun insertUserIfNotExists(user: User): Long {

        val existingUser = userDao.getUserByLogin(user.login.trim())
        return if (existingUser != null) {
            -1L
        } else {
            userDao.insertUser(user)
        }
    }


}
