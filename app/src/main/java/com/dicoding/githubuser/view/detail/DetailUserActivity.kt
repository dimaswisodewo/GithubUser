package com.dicoding.githubuser.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.model.UserDetails
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var sectionsPagerAdapter: DetailUserPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserDetails>(EXTRA_USER) as UserDetails
        bindData(user)

        setSectionsPagerAdapter()

        detailViewModel.isSnackbarShown.hasBeenHandled = false
        detailViewModel.getUserFollowers(user.username!!)
        detailViewModel.getUserFollowing(user.username!!)

        supportActionBar?.title = user.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun bindData(user: UserDetails) {
        val nullText = getString(R.string.null_text)
        with(binding) {
            with(user) {
                tvUserUsername.text = StringBuilder("@").append(username).toString()
                tvUserFullname.text = name ?: nullText
                tvUserLocation.text = location ?: nullText
                tvUserRepositories.text = StringBuilder(publicRepos?.toString() ?: nullText)
                    .append(" Repository").toString()
                tvUserCompany.text = company
                tvFollowingValue.text = following?.toString() ?: nullText
                tvFollowersValue.text = followers?.toString() ?: nullText

                Glide.with(applicationContext)
                    .load(avatarUrl)
                    .into(imgUserPhoto)
            }
        }
    }

    private fun setSectionsPagerAdapter() {
        sectionsPagerAdapter = DetailUserPagerAdapter(this)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_detail_1,
            R.string.tab_text_detail_2
        )
    }
}