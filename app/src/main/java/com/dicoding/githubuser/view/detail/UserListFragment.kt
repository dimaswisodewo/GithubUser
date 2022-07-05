package com.dicoding.githubuser.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentUserListBinding
import com.dicoding.githubuser.view.main.ListUserAdapter
import com.google.android.material.snackbar.Snackbar

class UserListFragment() : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    val binding get() = _binding!!

    lateinit var state: DetailUserPagerState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun subscribe() {
        val parent = activity as DetailUserActivity
        binding.rvListUser.layoutManager = LinearLayoutManager(parent)

        when (state) {
            DetailUserPagerState.FOLLOWING -> {
                parent.detailViewModel.listFollowing.observe(parent) {
                    binding.rvListUser.adapter = ListUserAdapter(it)
                }
            }
            DetailUserPagerState.FOLLOWERS -> {
                parent.detailViewModel.listFollowers.observe(parent) {
                    binding.rvListUser.adapter = ListUserAdapter(it)
                }
            }
        }

        parent.detailViewModel.isLoading.observe(parent) {
            with (binding) {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    rvListUser.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    rvListUser.visibility = View.VISIBLE
                }
            }
        }

        parent.detailViewModel.isError.observe(parent) {
            if (it) {
                parent.detailViewModel.isSnackbarShown.getContentIfNotHandled()?.let {
                    Snackbar.make(binding.root,
                        parent.detailViewModel.errorMessage,
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}