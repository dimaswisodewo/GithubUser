package com.dicoding.githubuser.view.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.databinding.ItemListUserBinding
import com.dicoding.githubuser.room.UserEntity

class ListFavoriteUserAdapter(private val listFavoriteUser: List<UserEntity>) :
    RecyclerView.Adapter<ListFavoriteUserAdapter.ListViewHolder>() {

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
            with(listFavoriteUser[position]) {
                binding.tvUserUsername.text = StringBuilder("@").append(login).toString()
                binding.tvUserFullname.text = fullName ?: "NULL"
                binding.tvUserLocation.text = location ?: "NULL"
                binding.tvUserRepositories.text = StringBuilder(publicRepos?.toString() ?: "NULL").append(" Repository").toString()
                binding.tvUserCompany.text = company ?: "NULL"

                Glide.with(holder.itemView)
                    .load(avatarUrl)
                    .into(binding.imgUserPhoto)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(listFavoriteUser[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }

    interface OnItemClickCallback {
        fun onItemClick(user: UserEntity) {

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}