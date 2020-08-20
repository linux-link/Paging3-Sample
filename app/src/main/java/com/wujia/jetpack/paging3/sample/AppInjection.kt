package com.wujia.jetpack.paging3.sample

import androidx.lifecycle.ViewModelProvider
import com.wujia.jetpack.paging3.sample.common.ViewModelFactory
import com.wujia.jetpack.paging3.sample.data.GithubService
import com.wujia.jetpack.paging3.sample.data.NetworkRepository

object AppInjection {
    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideNetworkRepository(): NetworkRepository {
        return NetworkRepository(
            GithubService.create()
        )
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideNetworkRepository()
        )
    }
}