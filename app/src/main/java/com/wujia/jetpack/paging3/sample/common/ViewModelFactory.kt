package com.wujia.jetpack.paging3.sample.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wujia.jetpack.paging3.sample.data.AppRepository
import com.wujia.jetpack.paging3.sample.ui.GithubViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {

    @ExperimentalCoroutinesApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GithubViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}