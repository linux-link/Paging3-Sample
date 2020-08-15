package com.wujia.jetpack.paging3.sample.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wujia.jetpack.paging3.sample.data.NetworkRepository
import com.wujia.jetpack.paging3.sample.ui.NetworkViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ViewModelFactory(private val repository: NetworkRepository) : ViewModelProvider.Factory {

    @ExperimentalCoroutinesApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}