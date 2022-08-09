package com.dicoding.githubuser.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.model.UserDetails
import com.dicoding.githubuser.room.UserEntity
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.dicoding.githubuser.viewmodel.FavoritesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var sectionsPagerAdapter: DetailUserPagerAdapter

    private var isFavorited: Boolean = false

    private var userEntity: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserDetails>(EXTRA_USER) as UserDetails
        bindData(user)

        favoritesViewModel = FavoritesViewModel(application)
        userEntity = favoritesViewModel.findByUsername(user.username!!)
        isFavorited = userEntity != null
        Log.d(DetailUserActivity::class.java.simpleName, "is userEntity of ${user.username} null ${userEntity == null}")

        if (isFavorited) setActiveFavoriteIcon(true)
        else setActiveFavoriteIcon(false)

        setSectionsPagerAdapter()

        detailViewModel.isSnackbarShown.hasBeenHandled = false
        detailViewModel.getUserFollowers(user.username!!)
        detailViewModel.getUserFollowing(user.username!!)

        supportActionBar?.title = user.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fabAdd.setOnClickListener {
            if (!isFavorited) {
                if (userEntity == null) {
                    userEntity = UserEntity(
                        login = user.username,
                        fullName = user.name,
                        location = user.location,
                        company = user.company,
                        publicRepos = user.publicRepos,
                        following = user.following,
                        followers = user.followers,
                        avatarUrl = user.avatarUrl
                    )
                }

                showAlertDialog(userEntity as UserEntity, FavoritesViewModel.ALERT_DIALOG_ADD)
            }
            else showAlertDialog(userEntity as UserEntity, FavoritesViewModel.ALERT_DIALOG_DELETE)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setActiveFavoriteIcon(isActive: Boolean) {
        if (isActive) {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
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

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(user: UserEntity, dialogType: Int) {
        var dialogTitle = ""
        var dialogMessage = ""
        when (dialogType) {
            FavoritesViewModel.ALERT_DIALOG_ADD -> {
                dialogTitle = "Favorite ${user.login}?"
                dialogMessage = "Are you sure you want to add ${user.login} to your favorites?"
            }
            FavoritesViewModel.ALERT_DIALOG_DELETE -> {
                dialogTitle = "Remove ${user.login}?"
                dialogMessage = "Are you sure you want to remove ${user.login} from your favorites?"
            }
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        with (alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Yes") { dialog, _ ->
                var snackBarMessage = ""
                when (dialogType) {
                    FavoritesViewModel.ALERT_DIALOG_ADD -> {
                        favoritesViewModel.insert(user)
                        setActiveFavoriteIcon(true)
                        isFavorited = true
                        snackBarMessage = "User ${user.login} added to favorites"
                    }
                    FavoritesViewModel.ALERT_DIALOG_DELETE -> {
                        favoritesViewModel.delete(user)
                        setActiveFavoriteIcon(false)
                        isFavorited = false
                        snackBarMessage = "User ${user.login} removed from favorites"
                    }
                }
                dialog.cancel()
                showSnackBar(snackBarMessage)
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
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