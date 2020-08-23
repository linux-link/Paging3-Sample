package com.wujia.jetpack.paging3.sample.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wujia.jetpack.paging3.sample.model.Repo

class GithubAdapter : PagingDataAdapter<Repo, RecyclerView.ViewHolder>(
    REPO_COMPARATOR
) {
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return (oldItem.fullName == newItem.fullName) ||
                        (oldItem.description == newItem.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GithubItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repo = getItem(position)
        (holder as GithubItemViewHolder).bind(repo)
    }

}