package com.wujia.jetpack.paging3.sample.component

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.wujia.jetpack.paging3.sample.data.AppRepository
import com.wujia.jetpack.paging3.sample.data.local.RepoDataBase
import com.wujia.jetpack.paging3.sample.data.remote.GithubService

object AppInjection {

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideGithubRepository(context: Context): AppRepository {
        return AppRepository(GithubService.create(), RepoDataBase.getInstance(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository(context))
    }

}