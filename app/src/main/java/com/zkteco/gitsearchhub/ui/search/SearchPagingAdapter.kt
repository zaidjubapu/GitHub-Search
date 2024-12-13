package com.zkteco.gitsearchhub.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zkteco.gitsearchhub.data.model.GitHubUser
import com.zkteco.gitsearchhub.databinding.ItemUserBinding

class SearchPagingAdapter(
    private val onUserClick: (GitHubUser) -> Unit
) : PagingDataAdapter<GitHubUser, SearchPagingAdapter.UserViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding,onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.bind(it) }
    }

    class UserViewHolder(private val binding: ItemUserBinding,
                         private val onUserClick: (GitHubUser) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GitHubUser) {
            binding.user = user
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<GitHubUser>() {
            override fun areItemsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}
