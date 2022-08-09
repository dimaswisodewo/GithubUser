package com.dicoding.githubuser.view.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityFavoritesBinding
import com.dicoding.githubuser.model.UserDetails
import com.dicoding.githubuser.room.UserEntity
import com.dicoding.githubuser.view.detail.DetailUserActivity
import com.dicoding.githubuser.viewmodel.FavoritesViewModel
import com.dicoding.githubuser.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoritesViewModel = obtainViewModel(this@FavoritesActivity)

        showLoading(true)
        showListUser(false)
        showTextNoResult(false)

        supportActionBar?.title = "Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_favorites -> {
                showAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun subscribe() {
        binding.rvListUser.layoutManager = LinearLayoutManager(this)
        binding.rvListUser.setHasFixedSize(true)
        favoritesViewModel.getAllUsers().observe(this) { listFavUsers ->
            if (listFavUsers != null) {

                showLoading(false)
                if (listFavUsers.isNotEmpty())  showListUser(true)
                else showTextNoResult(true)

                val adapter = ListFavoriteUserAdapter(listFavUsers)
                adapter.setOnItemClickCallback(object: ListFavoriteUserAdapter.OnItemClickCallback {
                    override fun onItemClick(user: UserEntity) {
                        val userDetails = UserDetails().apply {
                            name = user.fullName
                            username = user.login
                            avatarUrl = user.avatarUrl
                            location = user.location
                            company = user.company ?: resources.getString(R.string.null_text)
                            followers = user.followers
                            following = user.following
                            publicRepos = user.publicRepos
                        }

                        val detailIntent = Intent(this@FavoritesActivity, DetailUserActivity::class.java)
                        detailIntent.putExtra(DetailUserActivity.EXTRA_USER, userDetails)
                        startActivity(detailIntent)
                    }
                })

                binding.rvListUser.adapter = adapter
            }
        }
    }

    private fun showAlertDialog() {
        val dialogTitle = "Remove all users?"
        val dialogMessage = "Are you sure you want to remove all users from your favorites?"

        val alertDialogBuilder = AlertDialog.Builder(this)
        with (alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Yes") { dialog, _ ->
                favoritesViewModel.deleteAllUsers()
                val snackBarMessage = "All users removed from favorites"
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

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isActive: Boolean) {
        binding.progressBar.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    private fun showListUser(isActive: Boolean) {
        binding.rvListUser.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    private fun showTextNoResult(isActive: Boolean) {
        binding.tvNoFavorites.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoritesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoritesViewModel::class.java]
    }
}