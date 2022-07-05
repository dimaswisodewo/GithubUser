package com.dicoding.githubuser.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val login: String?,
    val company: String?,
    val id: Int?,
    val publicRepos: Int?,
    val url: String?,
    val followers: Int?,
    val avatarUrl: String?,
    val htmlUrl: String?,
    val following: Int?,
    val name: String?,
    val location: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(company)
        parcel.writeValue(id)
        parcel.writeValue(publicRepos)
        parcel.writeString(url)
        parcel.writeValue(followers)
        parcel.writeString(avatarUrl)
        parcel.writeString(htmlUrl)
        parcel.writeValue(following)
        parcel.writeString(name)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
