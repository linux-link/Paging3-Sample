package com.wujia.jetpack.paging3.sample.ui.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.wujia.jetpack.paging3.sample.R
import com.wujia.jetpack.paging3.sample.databinding.FooterLoadStateViewBinding

class NetLoadStateViewHolder(
    private val binding: FooterLoadStateViewBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.also {
            it.setOnClickListener {
                retry.invoke()
            }
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): NetLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_load_state_view, parent, false)
            val binding = FooterLoadStateViewBinding.bind(view)
            return NetLoadStateViewHolder(binding, retry)
        }
    }

}