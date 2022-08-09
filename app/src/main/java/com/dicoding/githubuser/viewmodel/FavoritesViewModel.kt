package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.room.UserEntity
import com.dicoding.githubuser.room.UserRepository

class FavoritesViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    fun insert(user: UserEntity) {
        Log.d(FavoritesViewModel::class.java.simpleName, "insert ${user.login}")
        mUserRepository.insert(user)
    }

    fun delete(user: UserEntity) {
        Log.d(FavoritesViewModel::class.java.simpleName, "delete ${user.login}")
        mUserRepository.delete(user)
    }

    fun getAllUsers() : LiveData<List<UserEntity>> {
        return mUserRepository.getAllUsers()
    }

    fun deleteAllUsers() {
        mUserRepository.deleteAllUsers()
    }

    fun findByUsername(username: String) : UserEntity? = mUserRepository.findByUsername(username)

    companion object {
        const val ALERT_DIALOG_ADD = 10
        const val ALERT_DIALOG_DELETE = 20
    }
}