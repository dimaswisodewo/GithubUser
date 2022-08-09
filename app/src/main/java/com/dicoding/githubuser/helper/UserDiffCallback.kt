package com.dicoding.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuser.room.UserEntity

class UserDiffCallback(private val mOldUserList: List<UserEntity>, private val mNewUserList: List<UserEntity>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUserList[oldItemPosition]
        val newUser = mNewUserList[newItemPosition]
        return oldUser.login == newUser.login && oldUser.publicRepos == newUser.publicRepos
                && oldUser.avatarUrl == newUser.avatarUrl && oldUser.fullName == newUser.fullName
                && oldUser.company == newUser.company && oldUser.location == newUser.location
                && oldUser.followers == newUser.followers && oldUser.following == newUser.following
    }
}