package com.dicoding.githubuser.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.databinding.ItemListUserBinding
import com.dicoding.githubuser.model.UserDetailResponse

class ListUserAdapter(private val listUser: List<UserDetailResponse>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListViewHolder(val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with (holder) {
            with(listUser[position]) {
                binding.tvUserUsername.text = StringBuilder("@").append(login).toString()
                binding.tvUserFullname.text = name ?: "NULL"
                binding.tvUserLocation.text = location ?: "NULL"
                binding.tvUserRepositories.text = StringBuilder(publicRepos?.toString() ?: "NULL").append(" Repository").toString()
                binding.tvUserCompany.text = company?.toString() ?: "NULL"

                Glide.with(holder.itemView)
                    .load(avatarUrl)
                    .into(binding.imgUserPhoto)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(listUser[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClick(user: UserDetailResponse) {

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}