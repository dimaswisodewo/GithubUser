package com.dicoding.githubuser.room

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<UserEntity>> {
        return mUserDao.getAllUsers()
    }

    fun findByUsername(username: String): UserEntity? {
        val task = executorService.submit<UserEntity?> {
            return@submit mUserDao.findByUsername(username)
        }
        return task.get()
    }

    fun insert(user: UserEntity) {
        executorService.execute { mUserDao.insert(user) }
    }

    fun delete(user: UserEntity) {
        executorService.execute { mUserDao.delete(user) }
    }

    fun deleteAllUsers() {
        executorService.execute { mUserDao.deleteAllUsers() }
    }
}