package com.wujia.jetpack.paging3.sample.ui.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.wujia.jetpack.paging3.sample.R
import com.wujia.jetpack.paging3.sample.databinding.FooterViewBinding

class FooterViewHolder(
    private val binding: FooterViewBinding,
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
        fun create(parent: ViewGroup, retry: () -> Unit): FooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.footer_view, parent, false)
            val binding = FooterViewBinding.bind(view)
            return FooterViewHolder(binding, retry)
        }
    }

}