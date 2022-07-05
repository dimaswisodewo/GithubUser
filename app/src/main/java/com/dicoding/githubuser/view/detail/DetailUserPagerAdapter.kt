package com.dicoding.githubuser.view.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailUserPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var fragment = UserListFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        fragment = UserListFragment()
        when (position) {
            0 -> fragment.state = DetailUserPagerState.FOLLOWING
            1 -> fragment.state = DetailUserPagerState.FOLLOWERS
        }
        return fragment
    }
}

enum class DetailUserPagerState {
    FOLLOWERS,
    FOLLOWING
}