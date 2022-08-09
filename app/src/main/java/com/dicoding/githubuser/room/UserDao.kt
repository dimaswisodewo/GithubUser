package com.dicoding.githubuser.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM userentity ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM userentity WHERE login LIKE :username LIMIT 1")
    fun findByUsername(username: String): UserEntity?

    @Query("DELETE FROM userentity")
    fun deleteAllUsers()
}