package com.wujia.jetpack.paging3.sample.ui.footer

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class NetLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<NetLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: NetLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetLoadStateViewHolder = NetLoadStateViewHolder.create(parent, retry)

}