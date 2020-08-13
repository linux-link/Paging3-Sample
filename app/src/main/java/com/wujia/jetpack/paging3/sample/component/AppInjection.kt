package com.wujia.jetpack.paging3.sample.component

import androidx.lifecycle.ViewModelProvider
import com.wujia.jetpack.paging3.sample.data.AppRepository
import com.wujia.jetpack.paging3.sample.data.remote.GitHubService

object AppInjection {

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideGithubRepository(): AppRepository {
        return AppRepository(GitHubService.create())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }

}