package com.dicoding.githubuser.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "login")
    var login: String? = null,

    @ColumnInfo(name = "full_name")
    var fullName: String? = null,

    @ColumnInfo(name = "location")
    var location: String? = null,

    @ColumnInfo(name = "company")
    var company: String? = null,

    @ColumnInfo(name = "public_repos")
    var publicRepos: Int? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "followers")
    var followers: Int? = null,

    @ColumnInfo(name = "following")
    var following: Int? = null,
) : Parcelable
