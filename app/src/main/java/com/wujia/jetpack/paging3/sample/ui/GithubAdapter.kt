package com.wujia.jetpack.paging3.sample.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wujia.jetpack.paging3.sample.R
import com.wujia.jetpack.paging3.sample.ui.separator.SeparatorViewHolder

class GithubAdapter : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(
    REPO_COMPARATOR
) {

    companion object {
        //TODO : DiffUtil的比对机制
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.RepoItem &&
                        newItem is UiModel.RepoItem &&
                        oldItem.repo.fullName == newItem.repo.fullName) ||
                        (oldItem is UiModel.SeparatorItem &&
                                newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_list) {
            GithubItemViewHolder.create(
                parent
            )
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (it) {
                is UiModel.RepoItem -> (holder as GithubItemViewHolder).bind(it.repo)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.description)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.item_list
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }


}