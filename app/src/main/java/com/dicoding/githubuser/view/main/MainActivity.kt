package com.dicoding.githubuser.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.model.UserDetailResponse
import com.dicoding.githubuser.model.UserDetails
import com.dicoding.githubuser.view.detail.DetailUserActivity
import com.dicoding.githubuser.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.github_users)

        subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(inputQuery: String?): Boolean {
                inputQuery?.let {
                    mainViewModel.isSnackbarShown.hasBeenHandled = false
                    mainViewModel.getSearchedUsers(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        return true
    }

    private fun subscribe() {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.isError.observe(this) {
            if (it) showError()
        }
        mainViewModel.searchedUserDetails.observe(this) {
            showRecyclerList(it)
        }
    }

    private fun showRecyclerList(response: List<UserDetailResponse>) {
        Log.d(MainActivity::class.java.simpleName, "Response count: ${response.count()}")

        if (response.count() <= 0) {
            binding.listPlaceholder.visibility = View.VISIBLE
            binding.rvListUser.visibility = View.GONE
            return
        }

        binding.listPlaceholder.visibility = View.GONE
        binding.rvListUser.visibility = View.VISIBLE

        val listUserAdapter = ListUserAdapter(response)
        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemClick(user: UserDetailResponse) {
                val userDetails = UserDetails().apply {
                    name = user.name
                    username = user.login
                    avatarUrl = user.avatarUrl
                    location = user.location
                    company = user.company?.toString() ?: resources.getString(R.string.null_text)
                    followers = user.followers
                    following = user.following
                    publicRepos = user.publicRepos
                }

                val detailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                detailIntent.putExtra(DetailUserActivity.EXTRA_USER, userDetails)
                startActivity(detailIntent)
            }
        })

        binding.rvListUser.layoutManager = LinearLayoutManager(this)
        binding.rvListUser.adapter = listUserAdapter
    }

    private fun showLoading(isActive: Boolean) {
        with (binding) {
            if (isActive) {
                progressBar.visibility = View.VISIBLE
                listPlaceholder.visibility = View.GONE
                rvListUser.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                if (rvListUser.childCount > 0) {
                    rvListUser.visibility = View.VISIBLE
                } else {
                    listPlaceholder.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showError() {
        mainViewModel.isSnackbarShown.getContentIfNotHandled()?.let {
            Snackbar.make(binding.root, mainViewModel.errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}